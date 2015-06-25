package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.ClientTeamSchedulingConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.ClientTeamSchedulingConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

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
    
   	@Override
    @Transactional
	public ClientTeamSchedulingConfiguration saveOrUpdate(
			ClientTeamSchedulingConfiguration clientTeamSchedulingConfiguration) {
   		if (clientTeamSchedulingConfiguration == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientTeamSchedulingConfiguration can not be null.");
        }
   		Team reloadTeam = teamService.reload(clientTeamSchedulingConfiguration.getTeam());
   		clientTeamSchedulingConfiguration.setTeam(reloadTeam);
   		return clientTeamSchedulingConfigurationPersistence.save(clientTeamSchedulingConfiguration);
   	}

	@Override
	public ClientTeamSchedulingConfiguration findByTeam(
			Team team) {
		  if (team.getId() == null) {
	            throw new InvalidDataAccessApiUsageException(
	                    "Team cannot be null"
	                    + "to find ClientTeamSchedulingConfiguration");
	        }
		  
		  ClientTeamSchedulingConfiguration teamSchedulingConfigDB = clientTeamSchedulingConfigurationPersistence.find(team.getId());
		 
		  if(teamSchedulingConfigDB == null){
			  Team reloadTeam = teamPersistence.reload(team);
			  ClientTeamSchedulingConfiguration teamSchedulingConfig = new ClientTeamSchedulingConfiguration();
			  //Default to true if not configure before
			  teamSchedulingConfig.setUseProvider(true);
			  teamSchedulingConfig.setUseLocation(true);
			  teamSchedulingConfig.setTeam(reloadTeam);
			  return teamSchedulingConfig;
		  }else{
			  return teamSchedulingConfigDB;
		  }
	               
	}


}
