package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;

/**
 * Spring Data Repository for ClientPasswordConfiguration Entity
 */
public interface ClientPasswordConfigurationRepository extends
        JpaRepository<ClientPasswordConfiguration, Long>,
        JpaSpecificationExecutor<ClientPasswordConfiguration> {

    /**
     * Find ClientPasswordConfiguration by passed in client
     * 
     * @param client
     *            to lookup
     * @return null or an existing ClientPasswordConfiguration
     */
    public ClientPasswordConfiguration findByClient(Client client);
}