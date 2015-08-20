package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;
import com.emmisolutions.emmimanager.model.Team;

/**
 * The ClientTeamSelfRegConfiguration Service
 */
public interface ClientTeamSelfRegConfigurationService {

    /**
     * Save or Update a ClientTeamSelfRegConfiguration
     *
     * @param clientTeamSelfRegConfiguration to save or update
     * @return an associated ClientTeamPhoneConfiguration
     */
    ClientTeamSelfRegConfiguration create(
            ClientTeamSelfRegConfiguration clientTeamSelfRegConfiguration);

    /**
     * Save or Update a ClientTeamSelfRegConfiguration
     *
     * @param clientTeamSelfRegConfiguration to save or update
     * @return an associated ClientTeamPhoneConfiguration
     */
    ClientTeamSelfRegConfiguration update(
            ClientTeamSelfRegConfiguration clientTeamSelfRegConfiguration);

    /**
     * Find an existing clientTeamSelfRegConfiguration by team id
     *
     * @param team to find
     * @return an existing clientTeamSelfRegConfiguration
     */
    ClientTeamSelfRegConfiguration findByTeam(Team team);

    /**
     * Find an existing clientTeamSelfRegConfiguration by code
     *
     * @param code self reg code to find with
     * @return an existing clientTeamSelfRegConfiguration
     */
    ClientTeamSelfRegConfiguration findByCode(String code);
}
