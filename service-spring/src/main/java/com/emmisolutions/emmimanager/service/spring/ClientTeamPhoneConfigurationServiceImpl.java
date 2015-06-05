package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.ClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.ClientTeamPhoneConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamPhoneConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.ClientTeamEmailConfigurationService;
import com.emmisolutions.emmimanager.service.ClientTeamPhoneConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;
import com.emmisolutions.emmimanager.service.ClientService;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.util.*;

/**
 * Service layer for ClientTeamEmailConfiguration
 * 
 */
@Service
public class ClientTeamPhoneConfigurationServiceImpl implements
				ClientTeamPhoneConfigurationService {

    @Resource
    TeamService teamService;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    DefaultClientTeamPhoneConfigurationPersistence defaultClientTeamPhoneConfigurationPersistence;

    @Resource
    ClientTeamPhoneConfigurationPersistence clientTeamPhoneConfigurationPersistence;
    
   	@Override
    @Transactional
	public ClientTeamPhoneConfiguration saveOrUpdate(
			ClientTeamPhoneConfiguration clientTeamPhoneConfiguration) {
   		if (clientTeamPhoneConfiguration == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientTeamPhoneConfiguration can not be null.");
        }
   		Team reloadTeam = teamService.reload(clientTeamPhoneConfiguration.getTeam());
   		clientTeamPhoneConfiguration.setTeam(reloadTeam);
   		return clientTeamPhoneConfigurationPersistence.save(clientTeamPhoneConfiguration);
   	}

	@Override
	public ClientTeamPhoneConfiguration findByTeam(
			Team team) {
		  if (team.getId() == null) {
	            throw new InvalidDataAccessApiUsageException(
	                    "Team cannot be null"
	                    + "to find ClientTeamEmailConfiguration");
	        }
		  
		  ClientTeamPhoneConfiguration teamPhoneConfigDB = clientTeamPhoneConfigurationPersistence.find(team.getId());
		 
		  if(teamPhoneConfigDB == null){
			  Team reloadTeam = teamPersistence.reload(team);
			  DefaultClientTeamPhoneConfiguration defaultConfig= defaultClientTeamPhoneConfigurationPersistence.find();
			  ClientTeamPhoneConfiguration teamPhoneConfig = new ClientTeamPhoneConfiguration();
			  teamPhoneConfig.setCollectPhone(defaultConfig.isCollectPhone());
			  teamPhoneConfig.setRequirePhone(defaultConfig.isRequirePhone());
			  teamPhoneConfig.setTeam(reloadTeam);
			  return teamPhoneConfig;
		  }else{
			  return teamPhoneConfigDB;
		  }
	               
	}


}
