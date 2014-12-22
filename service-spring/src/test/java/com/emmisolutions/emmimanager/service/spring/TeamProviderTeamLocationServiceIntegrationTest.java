package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamLocationTeamProviderSaveRequest;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocationSaveRequest;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderTeamLocationPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.TeamProviderTeamLocationService;

public class TeamProviderTeamLocationServiceIntegrationTest extends
		BaseIntegrationTest {

	@Resource
	TeamProviderTeamLocationService teamProviderTeamLocationService;

	@Resource
	ClientPersistence clientPersistence;
	
	@Resource
	TeamPersistence teamPersistence;
	
	@Resource
	LocationPersistence locationPersistence;
	
	@Resource
	ProviderPersistence providerPersistence;
	
	@Resource
	TeamLocationPersistence teamLocationPersistence;

	@Resource
	TeamProviderPersistence teamProviderPersistence;

	@Resource
	TeamProviderTeamLocationPersistence teamProviderTeamLocationPersistence;

	@Test
	public void findByTeamProvider() {
		Team team = makeNewRandomTeam();
		Provider provider = makeNewRandomProvider();
		Location location = makeNewRandomLocation();

		TeamLocation teamLocation = new TeamLocation(location, team);
		teamLocationPersistence.saveTeamLocation(teamLocation);

		TeamProvider teamProvider = new TeamProvider();
		teamProvider.setTeam(team);
		teamProvider.setProvider(provider);
		teamProviderPersistence.save(teamProvider);

		TeamProviderTeamLocation tptl = new TeamProviderTeamLocation();
		tptl.setTeamLocation(teamLocation);
		tptl.setTeamProvider(teamProvider);
		List<TeamProviderTeamLocation> tptls = new ArrayList<TeamProviderTeamLocation>();
		tptls.add(tptl);
		teamProviderTeamLocationPersistence.saveAll(tptls);
		
		teamProviderTeamLocationService.findByTeamProvider(teamProvider, null);
		
		teamProviderTeamLocationService.findByTeamLocation(teamLocation, null);
	}
	
	@Test
	public void testCreateTeamProviderTeamLocation() {
		Team team = makeNewRandomTeam();
		Provider provider = makeNewRandomProvider();
		Location location = makeNewRandomLocation();

		TeamLocation teamLocation = new TeamLocation(location, team);
		TeamLocation firstTeam = teamLocationPersistence.saveTeamLocation(teamLocation);

		TeamLocation teamLocationTwo = new TeamLocation(location, team);
		TeamLocation TeamTwo = teamLocationPersistence.saveTeamLocation(teamLocationTwo);
		
		Set<TeamLocation> teamLocations = new HashSet<TeamLocation>();
		teamLocations.add(teamLocation);
		teamLocations.add(teamLocationTwo);
		
		TeamProvider teamProvider = new TeamProvider();
		teamProvider.setTeam(team);
		teamProvider.setProvider(provider);
		teamProviderPersistence.save(teamProvider);

		teamProviderTeamLocationService.createTeamProviderTeamLocation(teamLocations, teamProvider);
	}

	@Test
	public void saveAllTeamProviderTeamLocations() {
		Team team = makeNewRandomTeam();
		Provider provider = makeNewRandomProvider();
		Location location = makeNewRandomLocation();
		Location locationA = makeNewRandomLocation();

		TeamLocation teamLocation = new TeamLocation(location, team);
		teamLocationPersistence.saveTeamLocation(teamLocation);
		
		TeamLocation teamLocationA = new TeamLocation(locationA, team);
		teamLocationPersistence.saveTeamLocation(teamLocationA);

		TeamProvider teamProvider = new TeamProvider();
		teamProvider.setTeam(team);
		teamProvider.setProvider(provider);
		teamProviderPersistence.save(teamProvider);

		TeamProviderTeamLocation tptl = new TeamProviderTeamLocation();
		tptl.setTeamLocation(teamLocation);
		tptl.setTeamProvider(teamProvider);
		
		TeamProviderTeamLocation tptlA = new TeamProviderTeamLocation();
		tptlA.setTeamLocation(teamLocationA);
		tptlA.setTeamProvider(teamProvider);
		List<TeamProviderTeamLocation> tptls = new ArrayList<TeamProviderTeamLocation>();
		tptls.add(tptl);
		tptls.add(tptlA);
		
		teamProviderTeamLocationService.saveAllTeamProviderTeamLocations(tptls);
	}

	@Test
	public void removeAllByTeamProvider() {
		Team team = makeNewRandomTeam();
		Provider provider = makeNewRandomProvider();
		Location location = makeNewRandomLocation();

		TeamLocation teamLocation = new TeamLocation(location, team);
		teamLocationPersistence.saveTeamLocation(teamLocation);

		TeamProvider teamProvider = new TeamProvider();
		teamProvider.setTeam(team);
		teamProvider.setProvider(provider);
		teamProviderPersistence.save(teamProvider);

		TeamProviderTeamLocation tptl = new TeamProviderTeamLocation();
		tptl.setTeamLocation(teamLocation);
		tptl.setTeamProvider(teamProvider);
		List<TeamProviderTeamLocation> tptls = new ArrayList<TeamProviderTeamLocation>();
		tptls.add(tptl);
		teamProviderTeamLocationPersistence.saveAll(tptls);
		
		teamProviderTeamLocationService.removeAllByTeamProvider(teamProvider);
	}

	@Test
	public void updateTeamProviderTeamLocationsByTeamLocation() {
		Team team = makeNewRandomTeam();
		Provider provider = makeNewRandomProvider();
		Provider providerA = makeNewRandomProvider();
		Location location = makeNewRandomLocation();

		TeamLocation teamLocation = new TeamLocation(location, team);
		teamLocationPersistence.saveTeamLocation(teamLocation);

		TeamProvider teamProvider = new TeamProvider();
		teamProvider.setTeam(team);
		teamProvider.setProvider(provider);
		teamProviderPersistence.save(teamProvider);
		
		TeamProvider teamProviderA = new TeamProvider();
		teamProviderA.setTeam(team);
		teamProviderA.setProvider(providerA);
		teamProviderPersistence.save(teamProviderA);

		TeamProviderTeamLocation tptl = new TeamProviderTeamLocation();
		tptl.setTeamLocation(teamLocation);
		tptl.setTeamProvider(teamProvider);
		List<TeamProviderTeamLocation> tptls = new ArrayList<TeamProviderTeamLocation>();
		tptls.add(tptl);
		teamProviderTeamLocationPersistence.saveAll(tptls);
		
		TeamLocationTeamProviderSaveRequest request = new TeamLocationTeamProviderSaveRequest();
		Set<TeamProvider> providers = new HashSet<TeamProvider>();
		providers.add(teamProvider);
		providers.add(teamProviderA);
		request.setProviders(providers);
		teamProviderTeamLocationService.updateTeamProviderTeamLocations(teamLocation, request);
	}

	@Test
	public void updateTeamProviderTeamLocationsByTeamProvider() {
		Team team = makeNewRandomTeam();
		Provider provider = makeNewRandomProvider();
		Location location = makeNewRandomLocation();
		Location locationA = makeNewRandomLocation();

		TeamLocation teamLocation = new TeamLocation(location, team);
		teamLocationPersistence.saveTeamLocation(teamLocation);
		
		TeamLocation teamLocationA = new TeamLocation(locationA, team);
		teamLocationPersistence.saveTeamLocation(teamLocationA);

		TeamProvider teamProvider = new TeamProvider();
		teamProvider.setTeam(team);
		teamProvider.setProvider(provider);
		teamProviderPersistence.save(teamProvider);

		TeamProviderTeamLocation tptl = new TeamProviderTeamLocation();
		tptl.setTeamLocation(teamLocation);
		tptl.setTeamProvider(teamProvider);
		List<TeamProviderTeamLocation> tptls = new ArrayList<TeamProviderTeamLocation>();
		tptls.add(tptl);
		teamProviderTeamLocationPersistence.saveAll(tptls);
		
		TeamProviderTeamLocationSaveRequest request = new TeamProviderTeamLocationSaveRequest();
		Set<TeamLocation> teamLocations = new HashSet<TeamLocation>();
		teamLocations.add(teamLocation);
		teamLocations.add(teamLocationA);
		request.setTeamLocations(teamLocations);
		teamProviderTeamLocationService.updateTeamProviderTeamLocations(teamProvider, request);
	}
	
}
