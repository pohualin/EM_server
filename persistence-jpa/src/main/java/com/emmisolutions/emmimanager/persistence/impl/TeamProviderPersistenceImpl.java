package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TeamProviderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * TeamProvider Persistence Implementation
 *
 */
@Repository
public class TeamProviderPersistenceImpl implements TeamProviderPersistence {

	@Resource
	TeamProviderRepository teamProviderRepository;

	@Override
	public TeamProvider reload(Long id) {
		return teamProviderRepository.findOne(id);
	}

	@Override
	public TeamProvider save(TeamProvider provider) {
		return teamProviderRepository.save(provider);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team) {
		return teamProviderRepository.findTeamProvidersByTeam(page, team);
	}

	@Override
	@Transactional
	public void delete(TeamProvider provider) {
		teamProviderRepository.delete(provider);
	}

	@Override
	@Transactional
	public List<TeamProvider> saveAll(Set<TeamProvider> teamProviders){
		return teamProviderRepository.save(teamProviders);
	}

    @Override
    public long delete(Client client, Provider provider) {
         return teamProviderRepository.deleteByTeamClientAndProvider(client, provider);
    }

    @Override
    public Page<Team> findTeamsBy(Client client, Provider provider, Pageable page) {
        return teamProviderRepository.findTeamsByClientAndProvider(client, provider, page);
    }
}
