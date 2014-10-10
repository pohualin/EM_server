package com.emmisolutions.emmimanager.persistence.impl;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ProviderRepository;

/**
 * Provider Persistence Implementation
 *
 */
@Repository
public class ProviderPersistenceImpl implements ProviderPersistence {
	
	@Resource
	ProviderRepository providerRepository;
	
	public Provider save(Provider provider){
		return providerRepository.save(provider);
	}
	
	public Provider reload(Long id){
		return providerRepository.findOne(id);		
	}

	public Page<Provider> findAllProvidersByTeam(Pageable pageble, Team team){
		return providerRepository.findByTeams(pageble, team);
	}

}
