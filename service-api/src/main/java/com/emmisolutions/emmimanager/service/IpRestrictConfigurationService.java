package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;

/**
 * Service to deal with IpRestrictConfiguration
 */
public interface IpRestrictConfigurationService {

    /**
     * Create a new IpRestrictConfiguration
     * 
     * @param ipRestrictConfiguration
     *            to create
     * @return created IpRestrictConfiguration
     */
    public IpRestrictConfiguration create(
            IpRestrictConfiguration ipRestrictConfiguration);

    /**
     * Delete an existing IpRestrictConfiguration
     * 
     * @param ipRestrictConfiguration
     *            to delete
     */
    public void delete(IpRestrictConfiguration ipRestrictConfiguration);

    /**
     * Get a page of IpRestrictConfiguration by Client
     * 
     * @param client
     *            to lookup
     * @return a page of IpRestrictConfiguration
     */
    public Page<IpRestrictConfiguration> getByClient(Pageable pageable,
            Client client);

    /**
     * Reload an existing IpRestrictConfiguration
     * 
     * @param ipRestrictConfiguration
     *            to reload
     * @return an existing IpRestrictConfiguration
     */
    public IpRestrictConfiguration reload(
            IpRestrictConfiguration ipRestrictConfiguration);

    /**
     * Update an existing IpRestrictConfiguration
     * 
     * @param ipRestrictConfiguration
     *            to update
     * @return an updated IpRestrictConfiguration
     */
    public IpRestrictConfiguration update(
            IpRestrictConfiguration ipRestrictConfiguration);

}
