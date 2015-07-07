package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientTeamSchedulingConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamSchedulingConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.ClientTeamSchedulingConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Service layer for ClientTeamSchedulingConfiguration
 * 
 */
@Service
public class ClientTeamSchedulingConfigurationServiceImpl implements
				ClientTeamSchedulingConfigurationService {

    @Resource
    TeamService teamService;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    ClientTeamSchedulingConfigurationPersistence clientTeamSchedulingConfigurationPersistence;
    
    @Resource
    DefaultClientTeamSchedulingConfigurationPersistence defaultClientTeamSchedulingConfigurationPersistence;
    
    @Override
    @Transactional
    public ClientTeamSchedulingConfiguration saveOrUpdate(
            ClientTeamSchedulingConfiguration clientTeamSchedulingConfiguration) {
        if (clientTeamSchedulingConfiguration == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientTeamSchedulingConfiguration can not be null.");
        }
        Team reloadTeam = teamService.reload(clientTeamSchedulingConfiguration
                .getTeam());
        clientTeamSchedulingConfiguration.setTeam(reloadTeam);
        clientTeamSchedulingConfiguration
                .setDefaultClientTeamSchedulingConfiguration(defaultClientTeamSchedulingConfigurationPersistence
                        .reload(clientTeamSchedulingConfiguration
                                .getDefaultClientTeamSchedulingConfiguration()
                                .getId()));
        return clientTeamSchedulingConfigurationPersistence
                .save(clientTeamSchedulingConfiguration);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientTeamSchedulingConfiguration findByTeam(Team team) {
        if (team.getId() == null) {
            throw new InvalidDataAccessApiUsageException("Team cannot be null"
                    + "to find ClientTeamSchedulingConfiguration");
        }

        ClientTeamSchedulingConfiguration teamSchedulingConfigDB = clientTeamSchedulingConfigurationPersistence
                .find(team.getId());

        if (teamSchedulingConfigDB == null) {
            Team reloadTeam = teamPersistence.reload(team);
            ClientTeamSchedulingConfiguration teamSchedulingConfig = new ClientTeamSchedulingConfiguration();
            // Set with DefaultClientTeamSchedulingConfiguration
            DefaultClientTeamSchedulingConfiguration configuration = defaultClientTeamSchedulingConfigurationPersistence
                    .findActive();
            teamSchedulingConfig.setDefaultClientTeamSchedulingConfiguration(configuration);
            teamSchedulingConfig.setTeam(reloadTeam);
            
            // Set default values
            teamSchedulingConfig.setUseProvider(configuration
                    .isDefaultUseProviders());
            teamSchedulingConfig.setUseLocation(configuration
                    .isDefaultUseLocations());
            teamSchedulingConfig.setUseViewByDays(configuration.isDefaultUseViewByDays());
            teamSchedulingConfig.setViewByDays(configuration.getDefaultViewByDays());;
            
            return teamSchedulingConfig;
        } else {
            return teamSchedulingConfigDB;
        }

    }

}
