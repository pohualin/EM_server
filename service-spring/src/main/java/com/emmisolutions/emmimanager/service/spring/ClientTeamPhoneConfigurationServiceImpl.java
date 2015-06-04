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
	public Page<ClientTeamPhoneConfiguration> findByTeam(
			Team team, Pageable pageable) {
		  if (team.getId() == null) {
	            throw new InvalidDataAccessApiUsageException(
	                    "Team cannot be null"
	                    + "to find ClientTeamEmailConfiguration");
	        }
		  
		  Page<ClientTeamPhoneConfiguration> teamPhoneConfigDB = clientTeamPhoneConfigurationPersistence.find(team.getId(), pageable);
		  if(!teamPhoneConfigDB.hasContent()){
			  Team reloadTeam = teamPersistence.reload(team);
			  List<ClientTeamPhoneConfiguration> list = new ArrayList<ClientTeamPhoneConfiguration> ();
			  Page<DefaultClientTeamPhoneConfiguration> deafaultClientPhone= defaultClientTeamPhoneConfigurationPersistence.findActive(pageable);
			  for(DefaultClientTeamPhoneConfiguration defaultConfig : deafaultClientPhone){
				  ClientTeamPhoneConfiguration teamPhoneConfig = new ClientTeamPhoneConfiguration();
				  teamPhoneConfig.setType(defaultConfig.getType());
				  teamPhoneConfig.setRank(defaultConfig.getRank());
				  teamPhoneConfig.setPhoneConfig(defaultConfig.isDefaultValue());
				  teamPhoneConfig.setTeam(reloadTeam);
				  list.add(teamPhoneConfig);
		  }
			  return new PageImpl<ClientTeamPhoneConfiguration>(list);
		  }
		  else{
			  return teamPhoneConfigDB;
		  }
	               
	}


}
