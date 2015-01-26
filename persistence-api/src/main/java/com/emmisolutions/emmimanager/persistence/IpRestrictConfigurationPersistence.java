package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;

/**
 * Persistence API for IpRestrictConfiguration
 */
public interface IpRestrictConfigurationPersistence {

    /**
     * Delete an IpRestrictConfiguration
     * 
     * @param id
     *            to delete
     *
     */
    public void delete(Long id);

    /**
     * Return a page of IpRestrictConfiguration
     * 
     * @param pageable
     *            to use
     * @param clientRestrictConfigurationId
     *            to lookup
     * @return a page of IpRestrictConfiguration
     */
    public Page<IpRestrictConfiguration> list(Pageable pageable,
            Long clientRestrictConfigurationId);

    /**
     * Reload IpRestricConfiguration by id
     * 
     * @param id
     *            to reload
     * @return an existing IpRestrictConfiguration
     */
    public IpRestrictConfiguration reload(Long id);

    /**
     * Save or update an IpRestrictConfiguration
     * 
     * @param ipRestrictConfiguration
     *            to save or update
     * @return saved or updated IpRestrictConfiguration
     */
    public IpRestrictConfiguration saveOrUpdate(
            IpRestrictConfiguration ipRestrictConfiguration);
}
