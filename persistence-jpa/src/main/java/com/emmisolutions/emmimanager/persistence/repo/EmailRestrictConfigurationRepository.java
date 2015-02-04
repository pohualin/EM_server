package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;

/**
 * Repository to deal with EmailRestrictConfiguration entity.
 */
public interface EmailRestrictConfigurationRepository extends
        JpaRepository<EmailRestrictConfiguration, Long>,
        JpaSpecificationExecutor<EmailRestrictConfiguration> {

}
