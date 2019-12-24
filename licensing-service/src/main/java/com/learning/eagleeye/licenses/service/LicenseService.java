package com.learning.eagleeye.licenses.service;

import com.learning.eagleeye.licenses.client.OrganizationRestTemplateClient;
import com.learning.eagleeye.licenses.model.License;
import com.learning.eagleeye.licenses.model.Organization;
import com.learning.eagleeye.licenses.repository.LicenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class LicenseService {

    private LicenseRepository licenseRepository;
    private OrganizationRestTemplateClient organizationClient;

    @Autowired
    public LicenseService(LicenseRepository licenseRepository, OrganizationRestTemplateClient organizationFeignClient) {
        this.licenseRepository = licenseRepository;
        this.organizationClient = organizationFeignClient;
    }

    public Optional<License> getLicense(String licenseId) {
        var maybeLicense = licenseRepository.findById(licenseId);
        maybeLicense.ifPresent(license -> getOrganization(license.getOrganizationId())
                .ifPresentOrElse(
                        org -> updateOrganizationDetails(license, org),
                        () -> license.setOrganizationId("<INVALID>")
                )
        );
        return maybeLicense;
    }

    public Iterable<License> getLicenses() {
        var licenses = licenseRepository.findAll();
        Map<String, Organization> organizationMap = new HashMap<>();
        for (var license : licenses) {
            if (organizationMap.containsKey(license.getOrganizationId())) {
                updateOrganizationDetails(license, organizationMap.get(license.getOrganizationId()));
            } else {
                var maybeOrg = getOrganization(license.getOrganizationId());
                maybeOrg.ifPresentOrElse(
                        org -> organizationMap.put(license.getOrganizationId(), org),
                        () -> license.setOrganizationId("<INVALID>")
                );
            }
        }
        return licenses;
    }

    public List<License> getLicenses(String organizationId) {
        var maybeOrg = getOrganization(organizationId);
        if (maybeOrg.isEmpty()) {
            return new ArrayList<>();
        }

        var licenses = licenseRepository.findByOrganizationId(organizationId);
        for (var license : licenses) {
            updateOrganizationDetails(license, maybeOrg.get());
        }
        return licenses;
    }

    public void saveLicense(License license) {
        license.setId(UUID.randomUUID().toString());
        licenseRepository.save(license);
    }

    public void updateLicense(License license) {
        licenseRepository.save(license);
    }

    public void deleteLicense(License license) {
        licenseRepository.deleteById(license.getId());
    }

    private void updateOrganizationDetails(License license, Organization org) {
        license.setOrganizationName(org.getName());
        license.setContactName(org.getContactName());
        license.setContactEmail(org.getContactEmail());
        license.setContactPhone(org.getContactPhone());
    }

    private Optional<Organization> getOrganization(String organizationId) {
        return organizationClient.getOrganization(organizationId);
    }
}
