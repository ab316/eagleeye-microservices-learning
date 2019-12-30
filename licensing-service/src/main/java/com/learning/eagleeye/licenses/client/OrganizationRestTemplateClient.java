package com.learning.eagleeye.licenses.client;

import com.learning.eagleeye.licenses.model.Organization;
import com.learning.eagleeye.licenses.repository.OrganizationRedisRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Slf4j
public class OrganizationRestTemplateClient {

    private RestTemplate template;
    private OrganizationRedisRepository organizationRepository;

    @Autowired
    public OrganizationRestTemplateClient(RestTemplate template, OrganizationRedisRepository organizationRepository) {
        this.template = template;
        this.organizationRepository = organizationRepository;
    }

    public Optional<Organization> getOrganization(String organizationId) {
        Optional<Organization> organization = organizationRepository.findById(organizationId);
        return organization.or(() -> {
            log.debug("Organization [{}] not found in cache. Fetching from Organization service", organizationId);
            Optional<Organization> fromService = getOrganizationFromService(organizationId);
            fromService.ifPresent(this::cacheOrganization);
            return fromService;
        });
    }

    @HystrixCommand(fallbackMethod = "buildFallbackOrganization",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            },
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
            })
    public Optional<Organization> getOrganizationFromService(String organizationId) {
        var entity = template.getForEntity(
                "http://zuul-server/api/organization/v1/organizations/{organizationId}",
                Organization.class, organizationId
        );
        return Optional.ofNullable(entity.getBody());
    }

    public Optional<Organization> buildFallbackOrganization(String organizationId, Throwable hysterixCommand) {
        log.warn("Failed to retrieve organization [{}]. Cause: [{}]", organizationId, hysterixCommand.getMessage());

        log.warn("Generating fallback Organization");
        String failedString = "FAILED TO RETRIEVE";
        return Optional.of(new Organization()
                .withId(organizationId)
                .withName(failedString)
                .withContactName(failedString)
                .withContactEmail(failedString)
                .withContactPhone(failedString)
        );
    }

    private void cacheOrganization(Organization organization) {
        organizationRepository.save(organization);
    }
}
