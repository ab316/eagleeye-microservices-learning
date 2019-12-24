package com.learning.eagleeye.licenses.client;

import com.learning.eagleeye.licenses.model.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient("organization-service")
public interface OrganizationFeignClient {
    @GetMapping(value = "/v1/organizations/{organizationId}", consumes = "application/json")
    Optional<Organization> getOrganization(@PathVariable String organizationId);
}
