package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.ClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.ClientTeamEmailConfigurationService;
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
public class ClientTeamEmailConfigurationServiceImpl implements
				ClientTeamEmailConfigurationService {

    @Resource
    TeamService teamService;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    DefaultClientTeamEmailConfigurationPersistence defaultClientTeamEmailConfigurationPersistence;

    @Resource
    ClientTeamEmailConfigurationPersistence clientTeamEmailConfigurationPersistence;
    
   	@Override
    @Transactional
	public ClientTeamEmailConfiguration saveOrUpdate(
			ClientTeamEmailConfiguration clientTeamEmailConfiguration) {
   		if (clientTeamEmailConfiguration == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientTeamEmailConfiguration can not be null.");
        }
   		
   		Team reloadTeam = teamService.reload(clientTeamEmailConfiguration.getTeam());
        clientTeamEmailConfiguration.setTeam(reloadTeam);
   		clientTeamEmailConfigurationPersistence.save(clientTeamEmailConfiguration);
        return clientTeamEmailConfigurationPersistence.save(clientTeamEmailConfiguration);
   	}

	@Override
	@Transactional
	public Page<ClientTeamEmailConfiguration> findByTeam(
			Team team, Pageable pageable) {
		  if (team.getId() == null) {
	            throw new InvalidDataAccessApiUsageException(
	                    "Team cannot be null"
	                    + "to find ClientTeamEmailConfiguration");
	        }
		  
		  Page<ClientTeamEmailConfiguration> teamEmailConfigDB = clientTeamEmailConfigurationPersistence.find(team.getId(), pageable);
		  if(!teamEmailConfigDB.hasContent()){
			  Team reloadTeam = teamPersistence.reload(team);
			  List<ClientTeamEmailConfiguration> list = new ArrayList<ClientTeamEmailConfiguration> ();
			  Page<DefaultClientTeamEmailConfiguration> deafaultClientEmail= defaultClientTeamEmailConfigurationPersistence.findActive(pageable);
			  for(DefaultClientTeamEmailConfiguration defaultConfig : deafaultClientEmail){
				  ClientTeamEmailConfiguration teamEmailConfig = new ClientTeamEmailConfiguration();
				  teamEmailConfig.setDescription(defaultConfig.getDescription());
				  teamEmailConfig.setType(defaultConfig.getType());
				  teamEmailConfig.setRank(defaultConfig.getRank());
				  teamEmailConfig.setEmailConfig(defaultConfig.isDefaultValue());
				  teamEmailConfig.setTeam(reloadTeam);
				  list.add(teamEmailConfig);
		  }
			  return new PageImpl<ClientTeamEmailConfiguration>(list);
		  }
		  else{
			  return teamEmailConfigDB;
		  }
	               
	}

	@Override
	@Transactional
	public ClientTeamEmailConfiguration reload(
			Long clientTeamEmailConfigurationId) {
		 if (clientTeamEmailConfigurationId == null) {
	            throw new InvalidDataAccessApiUsageException(
	                    "clientTeamEmailConfiguration Id can not be null.");
	        }
	        return clientTeamEmailConfigurationPersistence
	                .reload(clientTeamEmailConfigurationId);
	}

}
