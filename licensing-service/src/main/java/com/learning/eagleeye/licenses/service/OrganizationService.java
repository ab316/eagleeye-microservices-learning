package com.learning.eagleeye.licenses.service;

import brave.Tracer;
import com.learning.eagleeye.licenses.client.OrganizationRestTemplateClient;
import com.learning.eagleeye.licenses.model.Organization;
import com.learning.eagleeye.licenses.repository.OrganizationRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OrganizationService {

    private Tracer tracer;
    private OrganizationRedisRepository organizationRepository;
    private OrganizationRestTemplateClient client;

    @Autowired
    public OrganizationService(Tracer tracer, OrganizationRedisRepository organizationRepository, OrganizationRestTemplateClient client) {
        this.tracer = tracer;
        this.organizationRepository = organizationRepository;
        this.client = client;
    }

    public Optional<Organization> getOrganization(String organizationId) {
        Optional<Organization> organization = getOrganizationFromCache(organizationId);
        return organization.or(() -> {
            log.debug("Organization [{}] not found in cache. Fetching from Organization service", organizationId);
            Optional<Organization> fromService = client.getOrganization(organizationId);
            fromService.ifPresent(this::cacheOrganization);
            return fromService;
        });
    }

    private Optional<Organization> getOrganizationFromCache(String organizationId) {
        var span = tracer.startScopedSpan("readLicensingDataFromRedis");
        try {
            return organizationRepository.findById(organizationId);
        } catch (Exception ex) {
            log.warn("Failed to retrieve from cache: [{}]", ex.getMessage());
            return Optional.empty();
        } finally {
            span.tag("peer.service", "redis");
            span.finish();
        }
    }

    private void cacheOrganization(Organization organization) {
        try {
            organizationRepository.save(organization);
        } catch (Exception ex) {
            log.warn("Failed to cache: [{}]", ex.getMessage());
        }
    }
}
