package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.configuration.DefaultPasswordConfiguration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data Repository for DefaultPasswordConfiguration Entity
 */
public interface DefaultPasswordConfigurationRepository extends
        JpaRepository<DefaultPasswordConfiguration, Long>,
        JpaSpecificationExecutor<DefaultPasswordConfiguration> {

}
