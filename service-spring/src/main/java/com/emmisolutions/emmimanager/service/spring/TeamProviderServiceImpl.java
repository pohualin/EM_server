package com.emmisolutions.emmimanager.service.spring;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TeamProviderRepository;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.service.TeamProviderService;
import com.emmisolutions.emmimanager.service.TeamService;

@Service
public class TeamProviderServiceImpl implements TeamProviderService {

	@Resource
	TeamProviderPersistence teamProviderPersistence;

	@Resource
	TeamProviderRepository teamProviderRepository;

	@Resource
	ProviderService providerService;

	@Resource
	TeamService teamService;

	@Override
	@Transactional(readOnly = true)
	public TeamProvider reload(TeamProvider provider) {
		if (provider == null || provider.getId() == null) {
			return null;
		}
		return teamProviderPersistence.reload(provider.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team) {
		Team toFind = teamService.reload(team);
		return teamProviderPersistence.findTeamProvidersByTeam(page, toFind);
	}

	@Override
	@Transactional
	public void delete(TeamProvider provider) {
		TeamProvider fromDb = reload(provider);
		teamProviderPersistence.delete(fromDb);
	}

}
