package com.learning.eagleeye.licenses.controller;

import com.learning.eagleeye.licenses.model.License;
import com.learning.eagleeye.licenses.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

    private LicenseService licenseService;

    @Autowired
    public LicenseServiceController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping(value = "/")
    public List<License> getLicense(@PathVariable String organizationId) {
        return licenseService.getLicenses(organizationId);
    }

    @GetMapping(value = "/{licenseId}")
    public License getLicense(@PathVariable String organizationId, @PathVariable String licenseId) {
        return licenseService.getLicense(organizationId, licenseId);
    }

    @PostMapping(value = "/")
    public String saveLicense(@RequestBody License license) {
        licenseService.saveLicense(license);
        return license.getLicenseId();
    }

    @PutMapping(value = "/")
    public void updateLicense(@RequestBody License license) {
        licenseService.saveLicense(license);
    }

    @DeleteMapping(value = "/")
    public void deleteLicense(@RequestBody License license) {
        licenseService.deleteLicense(license);
    }
}
