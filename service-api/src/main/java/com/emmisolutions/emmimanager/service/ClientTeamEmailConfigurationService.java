package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.Team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    /**
     * Find a Page of existing clientTeamEmailConfiguration by Client id and Team id
     *
     * @param teamId to find
     * @param pageable to use
     * @return a Page of existing clientTeamEmailConfiguration
     */
    public Page<ClientTeamEmailConfiguration> findByTeam(
            Team teamId,
            Pageable pageable);

}
