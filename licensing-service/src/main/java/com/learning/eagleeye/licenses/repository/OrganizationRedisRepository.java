package com.learning.eagleeye.licenses.repository;

import com.learning.eagleeye.licenses.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRedisRepository extends CrudRepository<Organization, String> {
//    Organization findOrganization(String organizationId);
//    void saveOrganization(String organizationId);
//    void updateOrganization(String organizationId);
//    void deleteOrganization(String organizationId);
}
