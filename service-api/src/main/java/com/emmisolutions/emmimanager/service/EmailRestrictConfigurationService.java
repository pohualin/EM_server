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
     * @param emailRestrictConfiguration
     *            to create
     * @return created EmailRestrictConfiguration
     */
    public EmailRestrictConfiguration create(
            EmailRestrictConfiguration emailRestrictConfiguration);

    /**
     * Delete an existing EmailRestrictConfiguration
     * 
     * @param emailRestrictConfiguration
     *            to delete
     */
    public void delete(EmailRestrictConfiguration emailRestrictConfiguration);

    /**
     * Get a page of EmailRestrictConfiguration by Client
     * 
     * @param client
     *            to lookup
     * @return a page of EmailRestrictConfiguration
     */
    public Page<EmailRestrictConfiguration> getByClient(Pageable pageable,
            Client client);

    /**
     * Reload an existing EmailRestrictConfiguration
     * 
     * @param emailRestrictConfiguration
     *            to reload
     * @return an existing EmailRestrictConfiguration
     */
    public EmailRestrictConfiguration reload(
            EmailRestrictConfiguration emailRestrictConfiguration);

    /**
     * Update an existing EmailRestrictConfiguration
     * 
     * @param emailRestrictConfiguration
     *            to update
     * @return an updated EmailRestrictConfiguration
     */
    public EmailRestrictConfiguration update(
            EmailRestrictConfiguration emailRestrictConfiguration);

}
