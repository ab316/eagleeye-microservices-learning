package com.learning.eagleeye.organization.service;

import com.learning.eagleeye.organization.model.Organization;
import com.learning.eagleeye.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

    private OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    public Optional<Organization> get(String organizationId) {
        return organizationRepository.findById(organizationId);
    }

    public Iterable<Organization> get() {
        return organizationRepository.findAll();
    }

    public void save(Organization organization) {
        organization.setId(UUID.randomUUID().toString());
        organizationRepository.save(organization);
    }

    public void update(Organization organization) {
        organizationRepository.save(organization);
    }

    public void delete(Organization organization) {
        organizationRepository.deleteById(organization.getId());
    }
}
