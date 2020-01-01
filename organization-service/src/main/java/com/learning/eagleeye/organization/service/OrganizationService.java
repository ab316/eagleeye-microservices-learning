package com.learning.eagleeye.organization.service;

import brave.Tracer;
import com.learning.eagleeye.organization.events.source.SimpleSourceBean;
import com.learning.eagleeye.organization.model.Organization;
import com.learning.eagleeye.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

    private Tracer tracer;
    private OrganizationRepository organizationRepository;
    private SimpleSourceBean simpleSourceBean;

    @Autowired
    public OrganizationService(Tracer tracer, OrganizationRepository organizationRepository, SimpleSourceBean simpleSourceBean) {
        this.tracer = tracer;
        this.organizationRepository = organizationRepository;
        this.simpleSourceBean = simpleSourceBean;
    }

    public Optional<Organization> get(String organizationId) {
        var span = tracer.startScopedSpan("getOrgDbCall");
        try {
            return organizationRepository.findById(organizationId);
        } finally {
            span.tag("peer.service", "postgres");
            span.finish();
        }
    }

    public Iterable<Organization> get() {
        return organizationRepository.findAll();
    }

    public void save(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        organizationRepository.save(organization);
        simpleSourceBean.publishOrganizationChange("SAVE", organization.getId());
    }

    public void update(Organization organization) {
        organizationRepository.save(organization);
        simpleSourceBean.publishOrganizationChange("UPDATE", organization.getId());
    }

    public void delete(String organizationId) {
        try {
            if (organizationRepository.findById(organizationId).isPresent()) {
                organizationRepository.deleteById(organizationId);
                simpleSourceBean.publishOrganizationChange("DELETE", organizationId);
            }
        } catch (EmptyResultDataAccessException ignored) { }
    }
}
