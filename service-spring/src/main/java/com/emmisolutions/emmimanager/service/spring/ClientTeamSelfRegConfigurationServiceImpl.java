package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.persistence.ClientTeamPhoneConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.ClientTeamSelfRegConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamPhoneConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.ClientTeamPhoneConfigurationService;
import com.emmisolutions.emmimanager.service.ClientTeamSelfRegConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Service layer for ClientTeamEmailConfiguration
 * 
 */
@Service
public class ClientTeamSelfRegConfigurationServiceImpl implements
		ClientTeamSelfRegConfigurationService {

    @Resource
    TeamService teamService;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    DefaultClientTeamPhoneConfigurationPersistence defaultClientTeamPhoneConfigurationPersistence;

    @Resource
	ClientTeamSelfRegConfigurationPersistence clientTeamSelfRegConfigurationPersistence;
    
   	@Override
    @Transactional
	public ClientTeamSelfRegConfiguration saveOrUpdate(
			ClientTeamSelfRegConfiguration clientTeamSelfRegConfiguration) {
   		if (clientTeamSelfRegConfiguration == null) {
            throw new InvalidDataAccessApiUsageException(
                    "ClientTeamSelfRegConfiguration can not be null.");
        }

		//verify if it's a duplicate
		ClientTeamSelfRegConfiguration config = clientTeamSelfRegConfigurationPersistence.findByName(clientTeamSelfRegConfiguration.getCode());

		if (config != null && (clientTeamSelfRegConfiguration.getId() != null && config.getId() != clientTeamSelfRegConfiguration.getId())){
			throw new InvalidDataAccessApiUsageException(
					"self registration code is already in use");
		}

   		Team reloadTeam = teamService.reload(clientTeamSelfRegConfiguration.getTeam());
		clientTeamSelfRegConfiguration.setTeam(reloadTeam);
   		return clientTeamSelfRegConfigurationPersistence.save(clientTeamSelfRegConfiguration);
   	}

	@Override
	public ClientTeamSelfRegConfiguration findByTeam(
			Team team) {
		  if (team.getId() == null) {
	            throw new InvalidDataAccessApiUsageException(
	                    "Team cannot be null"
	                    + "to find ClientTeamSelfRegConfiguration");
	        }

		return clientTeamSelfRegConfigurationPersistence.find(team.getId());
	}
}
