package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for ClientContentSubscriptionConfiguration Entities
 */
public interface ClientContentSubscriptionConfigurationRepository extends
        JpaRepository<ClientContentSubscriptionConfiguration, Long>,
        JpaSpecificationExecutor<ClientContentSubscriptionConfiguration> {

    /**
     * Find a ClientContentSubscriptionConfiguration with given clientId and
     * page specification
     * 
     * @param clientId
     *            to use
     * @param pageable
     *            to use
     * @return a ClientContentSubscriptionConfiguration
     */
    Page<ClientContentSubscriptionConfiguration> findByClientId(
    		Long clientId, Pageable pageable);

}
