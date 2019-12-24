package com.learning.eagleeye.licenses.client;

import com.learning.eagleeye.licenses.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class OrganizationRestTemplateClient {

    private RestTemplate template;

    @Autowired
    public OrganizationRestTemplateClient(RestTemplate template) {
        this.template = template;
    }

    public Optional<Organization> getOrganization(String organizationId) {
        var entity = template.getForEntity("http://organization-service/v1/organizations/{organizationId}", Organization.class, organizationId);
        return Optional.ofNullable(entity.getBody());
    }
}
