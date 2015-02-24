package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;

/**
 * Repository to deal with IpRestrictConfiguration entity.
 */
public interface IpRestrictConfigurationRepository extends
        JpaRepository<IpRestrictConfiguration, Long>,
        JpaSpecificationExecutor<IpRestrictConfiguration> {

}
