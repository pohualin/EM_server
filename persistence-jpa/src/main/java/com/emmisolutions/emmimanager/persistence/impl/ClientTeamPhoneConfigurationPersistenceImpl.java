package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.ClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.ClientTeamPhoneConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ClientTeamEmailConfigurationRepository;
import com.emmisolutions.emmimanager.persistence.repo.ClientTeamPhoneConfigurationRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;


/**
 * Persistence implementation for ClientTeamPhoneConfiguration entity.
 */
@Repository
public class ClientTeamPhoneConfigurationPersistenceImpl implements
        ClientTeamPhoneConfigurationPersistence {

    @Resource
    ClientTeamPhoneConfigurationRepository clientTeamPhoneConfigurationRepository;

	@Override
	public ClientTeamPhoneConfiguration find(Long teamId){
		return clientTeamPhoneConfigurationRepository.findByTeamId(teamId);
	}

	@Override
	public ClientTeamPhoneConfiguration save(
			ClientTeamPhoneConfiguration clientTeamPhoneConfiguration) {
		return clientTeamPhoneConfigurationRepository.save(clientTeamPhoneConfiguration);
	}

	@Override
	public ClientTeamPhoneConfiguration reload(Long id) {
		 if (id == null) {
	            return null;
	    }
		return clientTeamPhoneConfigurationRepository.findOne(id);
	}

   
   
}
