package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;

/**
 * Persistence API for EmailRestrictConfiguration
 */
public interface EmailRestrictConfigurationPersistence {

    /**
     * Delete an EmailRestrictConfiguration
     * 
     * @param id
     *            to delete
     *
     */
    public void delete(Long id);

    /**
     * Return a page of EmailRestrictConfiguration
     * 
     * @param pageable
     *            to use
     * @param clientId
     *            to lookup
     * @return a page of EmailRestrictConfiguration
     */
    public Page<EmailRestrictConfiguration> list(Pageable pageable,
            Long clientId);

    /**
     * Reload EmailRestricConfiguration by id
     * 
     * @param id
     *            to reload
     * @return an existing EmailRestrictConfiguration
     */
    public EmailRestrictConfiguration reload(Long id);

    /**
     * Save or update an EmailRestrictConfiguration
     * 
     * @param emailRestrictConfiguration
     *            to save or update
     * @return saved or updated EmailRestrictConfiguration
     */
    public EmailRestrictConfiguration saveOrUpdate(
            EmailRestrictConfiguration emailRestrictConfiguration);
}
