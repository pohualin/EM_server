package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;

/**
 * Repository to deal with ClientRestrictConfiguration entity.
 */
public interface ClientRestrictConfigurationRepository extends
        JpaRepository<ClientRestrictConfiguration, Long>,
        JpaSpecificationExecutor<ClientRestrictConfiguration> {

    /**
     * Find ClientRestrictConfiguration by passed in Client
     * 
     * @param client
     *            to lookup
     * @return an existing ClientRestrictConfiguration
     */
    public ClientRestrictConfiguration findByClient(Client client);
}
