package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * The ClientTeamEmailConfiguration Service
 */
public interface ClientTeamEmailConfigurationService {

    /**
     * Save or Update a ClientTeamEmailConfiguration
     * 
     * @param clientTeamEmailConfiguration
     *            to save or update
     * @return a associated ClientTeamEmailConfiguration
     */
    public ClientTeamEmailConfiguration saveOrUpdate(
            ClientTeamEmailConfiguration clientTeamEmailConfiguration);

    
    /**
     * Find a Page of existing clientTeamEmailConfiguration by Client id  and
     * Team id
     * 
     * @param clientId
     *            to find
     * @param teamId
     *            to find
     * @param pageable
     *            to use
     * @return a Page of existing clientTeamEmailConfiguration
     */
    public Page<ClientTeamEmailConfiguration> findByClientIdAndTeamId(
            Long clientId, Long teamId,
            Pageable pageable);

 
     /**
     * Reload ClientTeamEmailConfiguration
     * 
     * @param clientTeamConfigurationId
     *            to reload
     * @return ClientTeamEmailConfiguration reloaded
     */
    public ClientTeamEmailConfiguration reload(
    		Long clientTeamConfigurationId);
}
