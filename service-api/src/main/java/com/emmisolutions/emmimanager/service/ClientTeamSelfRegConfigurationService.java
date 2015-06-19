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
     * @return a associated ClientTeamPhoneConfiguration
     */
    ClientTeamSelfRegConfiguration saveOrUpdate(
            ClientTeamSelfRegConfiguration clientTeamSelfRegConfiguration);


    /**
     * Find a existing clientTeamSelfRegConfiguration by team id
     *
     * @param team to find
     * @return a existing clientTeamSelfRegConfiguration
     */
    ClientTeamSelfRegConfiguration findByTeam(Team team);

    /**
     * Find a existing clientTeamSelfRegConfiguration by name/code
     *
     * @param name to find
     * @return a existing clientTeamSelfRegConfiguration
     */
//    ClientTeamSelfRegConfiguration findByName(String name);


}
