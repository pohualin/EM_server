package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
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
     * Get a page of EmailRestrictConfiguration by ClientRestrictConfiguration
     * 
     * @param clientRestrictConfiguration
     *            to lookup
     * @return a page of EmailRestrictConfiguration
     */
    public Page<EmailRestrictConfiguration> getByClientRestrictConfiguration(
            Pageable pageable,
            ClientRestrictConfiguration clientRestrictConfiguration);

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
