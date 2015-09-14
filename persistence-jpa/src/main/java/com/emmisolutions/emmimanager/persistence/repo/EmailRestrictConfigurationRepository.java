package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;

/**
 * Repository to deal with EmailRestrictConfiguration entity.
 */
public interface EmailRestrictConfigurationRepository extends
        JpaRepository<EmailRestrictConfiguration, Long>,
        JpaSpecificationExecutor<EmailRestrictConfiguration> {

    /**
     * Find an entry by email ending and client id
     *
     * @param emailEnding the email ending to search for
     * @param client the client to search for
     * @return an EmailRestrictConfiguration if the record is found
     */
    EmailRestrictConfiguration findByEmailEndingAndClient(String emailEnding, Client client);
}
