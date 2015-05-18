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
    ClientService clientService;
    
    @Resource
    TeamPersistence teamPersistence;

    @Resource
    ClientPersistence clientPersistence;
    
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
   		
   		Client reloadClient = clientService.reload(clientTeamEmailConfiguration.getClient());
        Team reloadTeam = teamService.reload(clientTeamEmailConfiguration.getTeam());
        clientTeamEmailConfiguration.setClient(reloadClient);
        clientTeamEmailConfiguration.setTeam(reloadTeam);
   		clientTeamEmailConfigurationPersistence.save(clientTeamEmailConfiguration);
        return clientTeamEmailConfigurationPersistence.save(clientTeamEmailConfiguration);
   	}

	@Override
	@Transactional
	public Page<ClientTeamEmailConfiguration> findByClientIdAndTeamId(
			Long clientId, Long teamId, Pageable pageable) {
		  if (clientId == null || teamId == null) {
	            throw new InvalidDataAccessApiUsageException(
	                    "Client and Team cannot be null"
	                    + "to find ClientTeamEmailConfiguration");
	        }
		  
		  Page<ClientTeamEmailConfiguration> teamEmailConfigDB = clientTeamEmailConfigurationPersistence.find(clientId, teamId, pageable);
		  if(!teamEmailConfigDB.hasContent()){
			  Client reloadClient = clientPersistence.reload(clientId);
		      Team reloadTeam = teamPersistence.reload(teamId);
			  List<ClientTeamEmailConfiguration> list = new ArrayList<ClientTeamEmailConfiguration> ();
			  Page<DefaultClientTeamEmailConfiguration> deafaultClientEmail= defaultClientTeamEmailConfigurationPersistence.findActive(pageable);
			  for(DefaultClientTeamEmailConfiguration defaultConfig : deafaultClientEmail){
				  ClientTeamEmailConfiguration teamEmailConfig = new ClientTeamEmailConfiguration();
				  teamEmailConfig.setDescription(defaultConfig.getDescription());
				  teamEmailConfig.setTeamEmailConfigurationId(defaultConfig.getId());
				  teamEmailConfig.setRank(defaultConfig.getRank());
				  teamEmailConfig.setValue(defaultConfig.isDefaultValue());
				  teamEmailConfig.setClient(reloadClient);
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
