package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.ClientTeamSchedulingConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientTeamSchedulingConfigurationRepository;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;


/**
 * Persistence implementation for ClientTeamSchedulingConfiguration entity.
 */
@Repository
public class ClientTeamSchedulingConfigurationPersistenceImpl implements
        ClientTeamSchedulingConfigurationPersistence {

    @Resource
    ClientTeamSchedulingConfigurationRepository clientTeamSchedulingConfigurationRepository;

	@Override
	public ClientTeamSchedulingConfiguration find(Long teamId){
		return clientTeamSchedulingConfigurationRepository.findByTeamId(teamId);
	}

	@Override
	public ClientTeamSchedulingConfiguration save(
			ClientTeamSchedulingConfiguration clientTeamSchedulingConfiguration) {
		return clientTeamSchedulingConfigurationRepository.save(clientTeamSchedulingConfiguration);
	}

	@Override
	public ClientTeamSchedulingConfiguration reload(Long id) {
		 if (id == null) {
	            return null;
	    }
		return clientTeamSchedulingConfigurationRepository.findOne(id);
	}

 
}
