package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.Team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The ClientTeamPhoneConfiguration Service
 */
public interface ClientTeamPhoneConfigurationService {

    /**
     * Save or Update a ClientTeamPhoneConfiguration
     * 
     * @param clientTeamPhoneConfiguration
     *            to save or update
     * @return a associated ClientTeamPhoneConfiguration
     */
    public ClientTeamPhoneConfiguration saveOrUpdate(
            ClientTeamPhoneConfiguration clientTeamPhoneConfiguration);

    
    /**
     * Find a existing clientTeamPhoneConfiguration by Client id  and
     * Team id
     * 
     * @param clientId
     *            to find
     * @param teamId
     *            to find
     * @return a existing clientTeamPhoneConfiguration
     */
    public ClientTeamPhoneConfiguration findByTeam(Team team);

     
}