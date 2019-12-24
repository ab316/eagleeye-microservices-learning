package com.learning.eagleeye.organization.controller;

import com.learning.eagleeye.organization.model.Organization;
import com.learning.eagleeye.organization.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/v1/organizations")
public class OrganizationServiceController {

    private OrganizationService organizationService;

    @Autowired
    public OrganizationServiceController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping(value = "/")
    public Iterable<Organization> get() {
        return organizationService.get();
    }

    @GetMapping(value = "/{organizationId}")
    public Organization get(@PathVariable String organizationId) {
        return organizationService.get(organizationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No organization with given id"));
    }

    @PostMapping(value = "/")
    public String save(@RequestBody Organization organization) {
        organizationService.save(organization);
        return organization.getId();
    }

    @PutMapping(value = "/")
    public void update(@RequestBody Organization organization) {
        organizationService.update(organization);
    }

    @DeleteMapping(value = "/")
    public void delete(@RequestBody Organization organization) {
        organizationService.delete(organization);
    }
}
