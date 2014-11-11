package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderTeamLocationPersistence;
import com.emmisolutions.emmimanager.service.*;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the TeamProviderService
 */
@Service
public class TeamProviderServiceImpl implements TeamProviderService {

	@Resource
	TeamProviderPersistence teamProviderPersistence;

	@Resource
	TeamService teamService;

    @Resource
    ClientService clientService;

    @Resource
    ProviderService providerService;

    @Resource
    ClientProviderService clientProviderService;

    @Resource
	TeamProviderTeamLocationService teamProviderTeamLocationService;

	@Resource
	TeamLocationService teamLocationService;

    @Resource
    TeamProviderTeamLocationPersistence teamProviderTeamLocationPersistence;

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
            throw new InvalidDataAccessApiUsageException("team cannot be null");
        }
		return teamProviderPersistence.findTeamProvidersByTeam(page, toFind);
	}

    @Override
    public Page<Team> findTeamsBy(Client client, Provider provider, Pageable page) {
        Client dbClient = clientService.reload(client);
        Provider dbProvider = providerService.reload(provider);
        if (dbClient == null || dbProvider == null){
            throw new InvalidDataAccessApiUsageException("Client and Provider must exist in the database");
        }
        return teamProviderPersistence.findTeamsBy(dbClient, dbProvider, page);
    }

    @Override
	@Transactional
	public void delete(TeamProvider provider) {
		TeamProvider fromDb = reload(provider);
		teamProviderTeamLocationService.removeAllByTeamProvider(fromDb);
        if (fromDb != null) {
            teamProviderPersistence.delete(fromDb);
        }
	}

    @Override
	@Transactional
	public Set<TeamProvider> associateProvidersToTeam(
			List<TeamProviderTeamLocationSaveRequest> request, Team team) {
		Team teamFromDb = teamService.reload(team);
		if (teamFromDb == null) {
			throw new InvalidDataAccessApiUsageException("Team cannot be null");
		}
		List<TeamProviderTeamLocation> teamProviderTeamLocationsToSave = new ArrayList<TeamProviderTeamLocation>();
		Set<TeamProvider> savedProviders = new HashSet<TeamProvider>();
		Set<Provider> providers = new HashSet<Provider>();

		for (TeamProviderTeamLocationSaveRequest req : request) {
			TeamProvider teamProvider = new TeamProvider();
			teamProvider.setId(null);
			teamProvider.setVersion(null);
			teamProvider.setProvider(req.getProvider());
			teamProvider.setTeam(teamFromDb);
			TeamProvider savedTeamProvider = teamProviderPersistence.save(teamProvider);
			savedProviders.add(savedTeamProvider);
			providers.add(req.getProvider());

			if (req.getTeamLocations() != null && !req.getTeamLocations().isEmpty()) {
				for (TeamLocation teamLocation : req.getTeamLocations()) {
					TeamLocation savedTeamLocation = teamLocationService.reload(teamLocation);
					TeamProviderTeamLocation tptl = new TeamProviderTeamLocation();
					tptl.setTeamProvider(savedTeamProvider);
					tptl.setTeamLocation(savedTeamLocation);
					teamProviderTeamLocationsToSave.add(tptl);
				}
			}
		}

		// create ClientProviders from new TeamProvider associations
    	clientProviderService.create(teamFromDb.getClient(), providers);

		List<TeamProviderTeamLocation> savedTptls = teamProviderTeamLocationService.saveAllTeamProviderTeamLocations(teamProviderTeamLocationsToSave);
		return savedProviders;
	}


    @Override
    @Transactional
    public long delete(Client client, Provider provider) {
        Client dbClient = clientService.reload(client);
        Provider dbProvider = providerService.reload(provider);
        if (dbClient == null || dbProvider == null){
            throw new InvalidDataAccessApiUsageException("Client and Provider must exist in the database");
        }
        teamProviderTeamLocationPersistence.removeAllByClientProvider(client, provider);
        return teamProviderPersistence.delete(dbClient, dbProvider);
    }

}
