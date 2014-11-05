package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import com.emmisolutions.emmimanager.service.TeamProviderService;
import com.emmisolutions.emmimanager.service.TeamService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
/**
 * Implementation of the TeamProviderService
 */
@Service
public class TeamProviderServiceImpl implements TeamProviderService {

	@Resource
	TeamProviderPersistence teamProviderPersistence;

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
		if (toFind == null) {
            throw new IllegalArgumentException("team cannot be null");
        }
		return teamProviderPersistence.findTeamProvidersByTeam(page, toFind);
	}

	@Override
	@Transactional
	public void delete(TeamProvider provider) {
		TeamProvider fromDb = reload(provider);
        if (fromDb != null) {
            teamProviderPersistence.delete(fromDb);
        }
	}

	@Override
	@Transactional
	public List<TeamProvider> associateProvidersToTeam(List<Provider> providers, Team team){
		Team teamFromDb = teamService.reload(team);
		if (teamFromDb == null) {
            throw new InvalidDataAccessApiUsageException("Team cannot be null");
        }
		List<TeamProvider> providersToSave = new ArrayList<TeamProvider>();
		for (Provider provider: providers){
			TeamProvider teamProvider = new TeamProvider();
			teamProvider.setId(null);
			teamProvider.setVersion(null);
			teamProvider.setProvider(provider);
			teamProvider.setTeam(teamFromDb);
			providersToSave.add(teamProvider);
		}
		return saveAll(providersToSave);
	}

	@Override
	@Transactional
	public List<TeamProvider> saveAll(List<TeamProvider> providers) {
          return  teamProviderPersistence.saveAll(providers);
	}

}
