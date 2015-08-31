package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.Team;

/**
 * The ClientTeamEmailConfiguration Service
 */
public interface ClientTeamEmailConfigurationService {

    /**
     * Save or Update a ClientTeamEmailConfiguration
     *
     * @param clientTeamEmailConfiguration to save or update
     * @return a associated ClientTeamEmailConfiguration
     */
    public ClientTeamEmailConfiguration saveOrUpdate(
            ClientTeamEmailConfiguration clientTeamEmailConfiguration);

    /**
     * Find an existing clientTeamEmailConfiguration by Team id
     *
     * @param teamId to find
     * @return an existing clientTeamEmailConfiguration
     */
    public ClientTeamEmailConfiguration findByTeam(Team teamId);

}
