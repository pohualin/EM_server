package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.Team;

/**
 * The ClientTeamPhoneConfiguration Service
 */
public interface ClientTeamPhoneConfigurationService {

    /**
     * Save or Update a ClientTeamPhoneConfiguration
     *
     * @param clientTeamPhoneConfiguration to save or update
     * @return a associated ClientTeamPhoneConfiguration
     */
    public ClientTeamPhoneConfiguration saveOrUpdate(ClientTeamPhoneConfiguration clientTeamPhoneConfiguration);

    /**
     * Find an existing clientTeamPhoneConfiguration by team id
     *
     * @param team to find
     * @return an existing clientTeamPhoneConfiguration
     */
    public ClientTeamPhoneConfiguration findByTeam(Team team);

}
