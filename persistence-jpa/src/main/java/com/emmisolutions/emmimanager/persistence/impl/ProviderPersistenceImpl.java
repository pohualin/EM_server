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

	@Override
	public Provider save(Provider provider) {
		return providerRepository.save(provider);
	}

	@Override
	public Provider reload(Long id) {
		return providerRepository.findOne(id);
	}

	@Override
	public Page<Provider> findAllProvidersByTeam(Pageable pageble, Team team) {
		return providerRepository.findByTeams(pageble, team);
	}

}
