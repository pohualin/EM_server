package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.configuration.PasswordConfiguration;

/**
 * Spring Data Repository for PasswordConfiguration Entity
 */
public interface PasswordConfigurationRepository extends
        JpaRepository<PasswordConfiguration, Long>,
        JpaSpecificationExecutor<PasswordConfiguration> {

}
