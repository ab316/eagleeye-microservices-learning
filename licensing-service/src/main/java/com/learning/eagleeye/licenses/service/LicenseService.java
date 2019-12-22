package com.learning.eagleeye.licenses.service;

import com.learning.eagleeye.licenses.model.License;
import com.learning.eagleeye.licenses.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {

    private LicenseRepository licenseRepository;

    @Autowired
    public LicenseService(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    public License getLicense(String organizationId, String licenseId) {
        return licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
    }

    public List<License> getLicenses(String organizationId) {
        return licenseRepository.findByOrganizationId(organizationId);
    }

    public void saveLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
    }

    public void updateLicense(License license) {
        licenseRepository.save(license);
    }

    public void deleteLicense(License license) {
        licenseRepository.deleteById(license.getLicenseId());
    }
}
