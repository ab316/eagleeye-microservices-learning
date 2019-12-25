package com.learning.eagleeye.licenses.controller;

import com.learning.eagleeye.licenses.model.License;
import com.learning.eagleeye.licenses.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/licenses")
public class LicenseServiceController {

    private LicenseService licenseService;

    @Autowired
    public LicenseServiceController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping(value = "/{licenseId}")
    public Optional<License> getLicense(@PathVariable String licenseId) {
        return licenseService.getLicense(licenseId);
    }

    @GetMapping(value = "/organization/{organizationId}")
    public List<License> getLicenses(@PathVariable String organizationId) {
        return licenseService.getLicenses(organizationId);
    }

    @PostMapping(value = "/")
    public String saveLicense(@RequestBody License license) {
        licenseService.saveLicense(license);
        return license.getId();
    }

    @PutMapping(value = "/")
    public void updateLicense(@RequestBody License license) {
        licenseService.updateLicense(license);
    }

    @DeleteMapping(value = "/")
    public void deleteLicense(@RequestBody License license) {
        licenseService.deleteLicense(license);
    }
}
