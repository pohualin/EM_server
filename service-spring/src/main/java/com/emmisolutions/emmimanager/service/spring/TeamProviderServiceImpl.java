package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocationSaveRequest;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderTeamLocationPersistence;
import com.emmisolutions.emmimanager.service.ClientProviderService;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.service.TeamLocationService;
import com.emmisolutions.emmimanager.service.TeamProviderService;
import com.emmisolutions.emmimanager.service.TeamProviderTeamLocationService;
import com.emmisolutions.emmimanager.service.TeamService;

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

		Set<TeamProviderTeamLocation> savedTptls = teamProviderTeamLocationService.saveAllTeamProviderTeamLocations(teamProviderTeamLocationsToSave);
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
    
	@Override
	@Transactional
	public Page<TeamProviderTeamLocation> findTeamLocationsByTeamProvider(
			TeamProvider teamProvider, Pageable pageable) {
		return teamProviderTeamLocationService.findByTeamProvider(teamProvider,
				pageable);
	}
    
	@Override
	@Transactional
	public void updateTeamProvider(TeamProviderTeamLocationSaveRequest request) {
		// Update existing provider
		providerService.update(request.getProvider());

		// Reload ClientProvider before we save external id then save it
		if (request.getClientProvider() != null) {
			ClientProvider toBeSaved = clientProviderService.reload(request
					.getClientProvider());
			toBeSaved
					.setExternalId(request.getClientProvider().getExternalId());
			clientProviderService.save(toBeSaved);
		}

		// Deal with TeamProviderTeamLocation relationship
		teamProviderTeamLocationService.updateTeamProviderTeamLocations(request.getTeamProvider(), request);
	}

	@Override
    @Transactional(readOnly = true)
    public Page<TeamProvider> findPossibleProvidersToAdd(Team team, ProviderSearchFilter providerSearchFilter, Pageable pageable) {
        if (team == null) {
            throw new InvalidDataAccessApiUsageException("Team cannot be null");
        }

        // find matching providers
        Page<Provider> matchedProviders = providerService.list(pageable, providerSearchFilter);

        // find TeamProviders for the page of matching providers
        Map<Provider, TeamProvider> matchedTeamProviderMap = new HashMap<>();
        for (TeamProvider matchedTeamProvider : teamProviderPersistence.getByTeamIdAndProviders(team.getId(), matchedProviders)) {
        	matchedTeamProvider.setTeam(team);
        	matchedTeamProviderMap.put(matchedTeamProvider.getProvider(), matchedTeamProvider);
        }

        // make TeamProvider objects from matched providers (from search) and the existing team providers
        List<TeamProvider> teamProviders = new ArrayList<>();
        for (Provider matchingProvider : matchedProviders) {
        	TeamProvider alreadyAssociated = matchedTeamProviderMap.get(matchingProvider);
        	teamProviders.add(alreadyAssociated != null ? alreadyAssociated : new TeamProvider(null, matchingProvider));
        }
        return new PageImpl<>(teamProviders, pageable, matchedProviders.getTotalElements());
    }

}
