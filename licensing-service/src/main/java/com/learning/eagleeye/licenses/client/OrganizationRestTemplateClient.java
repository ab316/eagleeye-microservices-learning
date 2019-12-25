package com.learning.eagleeye.licenses.client;

import com.learning.eagleeye.licenses.model.Organization;
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

    @Autowired
    public OrganizationRestTemplateClient(RestTemplate template) {
        this.template = template;
    }

    @HystrixCommand(fallbackMethod = "buildFallbackOrganization",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            },
            commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")
    })
    public Optional<Organization> getOrganization(String organizationId) {
        var entity = template.getForEntity("http://organization-service/v1/organizations/{organizationId}", Organization.class, organizationId);
        return Optional.ofNullable(entity.getBody());
    }

    public Optional<Organization> buildFallbackOrganization(String organizationId, Throwable hysterixCommand) {
        log.warn("Failed to retrieve organization [{}]. Cause: [{}]", organizationId, hysterixCommand.getMessage());

        log.warn("Generating fallback Organization");
        return Optional.of(new Organization()
                .withId(organizationId)
                .withName("FAILED TO RETRIEVE")
                .withContactName("FAILED TO RETRIEVE")
                .withContactEmail("FAILED TO RETRIEVE")
                .withContactPhone("FAILED TO RETRIEVE")
        );
    }
}
