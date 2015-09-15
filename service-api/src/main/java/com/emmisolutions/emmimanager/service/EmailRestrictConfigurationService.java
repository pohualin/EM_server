package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;

/**
 * Service to deal with EmailRestrictConfiguration
 */
public interface EmailRestrictConfigurationService {

    /**
     * Create a new EmailRestrictConfiguration
     * 
     * @param emailRestrictConfiguration to create
     * @return created EmailRestrictConfiguration
     */
    EmailRestrictConfiguration create(EmailRestrictConfiguration emailRestrictConfiguration);

    /**
     * Delete an existing EmailRestrictConfiguration
     * 
     * @param emailRestrictConfiguration to delete
     */
    void delete(EmailRestrictConfiguration emailRestrictConfiguration);

    /**
     * Get a page of EmailRestrictConfiguration by Client
     *
     * @param client to lookup
     * @return a page of EmailRestrictConfiguration
     */
    Page<EmailRestrictConfiguration> getByClient(Pageable pageable, Client client);

    /**
     * Reload an existing EmailRestrictConfiguration
     * 
     * @param emailRestrictConfiguration to reload
     * @return an existing EmailRestrictConfiguration
     */
    EmailRestrictConfiguration reload(EmailRestrictConfiguration emailRestrictConfiguration);

    /**
     * Update an existing EmailRestrictConfiguration
     * 
     * @param emailRestrictConfiguration to update
     * @return an updated EmailRestrictConfiguration
     */
    EmailRestrictConfiguration update(EmailRestrictConfiguration emailRestrictConfiguration);

    /**
     * Check if the passed in email ending already exists for a client
     *
     * @param emailRestrictConfiguration to search for
     * @return true if an entry already exists, false otherwise
     */
    boolean hasDuplicateEmailEnding(EmailRestrictConfiguration emailRestrictConfiguration);
}
