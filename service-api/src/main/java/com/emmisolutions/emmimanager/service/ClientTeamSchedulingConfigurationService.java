package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.Team;

/**
 * The ClientTeamSchedulingConfiguration Service
 */
public interface ClientTeamSchedulingConfigurationService {

    /**
     * Save or Update a ClientTeamSchedulingConfiguration
     * 
     * @param clientTeamSchedulingConfiguration
     *            to save or update
     * @return a associated ClientTeamSchedulingConfiguration
     */
    public ClientTeamSchedulingConfiguration saveOrUpdate(
            ClientTeamSchedulingConfiguration clientTeamSchedulingConfiguration);

    
    /**
     * Find a existing clientTeamSchedulingConfiguration by Client id  and
     * Team id
     * 
     * @param clientId
     *            to find
     * @param teamId
     *            to find
     * @return a existing clientTeamSchedulingConfiguration
     */
    public ClientTeamSchedulingConfiguration findByTeam(Team team);

     
}
