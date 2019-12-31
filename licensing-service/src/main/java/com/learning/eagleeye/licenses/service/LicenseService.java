package com.learning.eagleeye.licenses.service;

import com.learning.eagleeye.licenses.model.License;
import com.learning.eagleeye.licenses.model.Organization;
import com.learning.eagleeye.licenses.repository.LicenseRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class LicenseService {

    private OrganizationService organizationService;
    private LicenseRepository licenseRepository;

    @Autowired
    public LicenseService(OrganizationService organizationService, LicenseRepository licenseRepository) {
        this.organizationService = organizationService;
        this.licenseRepository = licenseRepository;
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

    @HystrixCommand(
            fallbackMethod = "fallbackGetLicensesByOrg",
            threadPoolKey = "licensesByOrgThreadPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            },
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
            }
            // See https://github.com/Netflix/Hystrix/wiki/Configuration for details
    )
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
        return organizationService.getOrganization(organizationId);
    }

    public List<License> fallbackGetLicensesByOrg(String organizationId, Throwable throwable) {
        log.warn("Generating fallback license due to: [{}]", throwable.getMessage());
        List<License> licenses = new ArrayList<>();
        licenses.add(new License()
                .withId("0")
                .withOrganizationId(organizationId)
                .withProductName("Failed to retrieve license"));
        return licenses;
    }
}
