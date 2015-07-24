package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.ClientLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderTeamLocationPersistence;
import com.emmisolutions.emmimanager.service.*;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of TeamLocationService
 */
@Service
public class TeamLocationServiceImpl implements TeamLocationService {

    @Resource
    TeamLocationPersistence teamLocationPersistence;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    TeamProviderService teamProviderService;
    
    @Resource
    LocationService locationService;

    @Resource
    ClientService clientService;

    @Resource
    ClientLocationPersistence clientLocationPersistence;

    @Resource
    ClientLocationService clientLocationService;

    @Resource
    TeamProviderTeamLocationPersistence teamProviderTeamLocationPersistence;

    @Override
    public Page<TeamLocation> findAllTeamLocationsWithTeam(Pageable pageable, Team team) {
        return teamLocationPersistence.getAllTeamLocationsForTeam(pageable, teamPersistence.reload(team));
    }

    @Override
    @Transactional
    public Set<TeamProviderTeamLocation> save(Team team, Set<TeamLocationTeamProviderSaveRequest> request) {
        Set<TeamProviderTeamLocation> savedTptls = new HashSet<>();
        Team teamToFind = teamPersistence.reload(team);
        if(teamToFind != null && request != null) {
            for (TeamLocationTeamProviderSaveRequest req : request) {
            	Location location = req.getLocation();
                TeamLocation teamLocation = new TeamLocation(location, teamToFind);
                teamLocationPersistence.saveTeamLocation(teamLocation);

                List<TeamProviderTeamLocation> teamProviderTeamLocationsToSave = new ArrayList<>();

				for (TeamProvider teamProvider : req.getProviders()) {
					TeamProviderTeamLocation tptl = new TeamProviderTeamLocation();
					TeamProvider teamProviderFromDb = teamProviderService.reload(teamProvider);
					TeamLocation teamLocationFromDb = teamLocationPersistence.reload(teamLocation);
					if(teamProviderFromDb == null || teamLocationFromDb == null) {
			            throw new InvalidDataAccessApiUsageException("TeamProvider and TeamLocation must exist in the database");
					}
					tptl.setTeamProvider(teamProviderFromDb);
					tptl.setTeamLocation(teamLocationFromDb);
					teamProviderTeamLocationsToSave.add(tptl);
				}

				if (teamProviderTeamLocationsToSave.size() > 0 ){
					savedTptls = teamProviderTeamLocationPersistence.saveAll(teamProviderTeamLocationsToSave);
				}
                // add to client location as well
                clientLocationPersistence.create(location.getId(), teamToFind.getClient().getId());
            }
        }

        return savedTptls;
    }

    @Override
    public TeamLocation reload(TeamLocation teamLocation){
        return teamLocationPersistence.reload(teamLocation);
    }

    @Override
    @Transactional
    public void delete(TeamLocation teamLocation){
    	teamLocation = teamLocationPersistence.reload(teamLocation);
    	teamLocationPersistence.deleteTeamLocation(teamLocation);
    }

    @Override
    public Page<Team> findTeamsBy(Client client, Location location, Pageable page) {
        Client dbClient = clientService.reload(client);
        Location dbLocation = locationService.reload(location);
        if (dbClient == null || dbLocation == null){
            throw new InvalidDataAccessApiUsageException("Client and Location must exist in the database");
        }
        return teamLocationPersistence.findTeamsBy(dbClient, dbLocation, page);
    }

    @Override
    @Transactional
    public long delete(Client client, Location location) {
        Client dbClient = clientService.reload(client);
        Location dbLocation = locationService.reload(location);
        if (dbClient == null || dbLocation == null){
            throw new InvalidDataAccessApiUsageException("Client and Location must exist in the database");
        }
        teamProviderTeamLocationPersistence.removeAllByClientLocation(client, location);
        return teamLocationPersistence.delete(dbClient, dbLocation);
    }

    @Override
    @Transactional
    public Set<TeamLocation> associateAllClientLocationsExcept(Team team,
                                                               Set<Long> excludeSet) {
        Set<TeamLocation> added = new HashSet<>();
        team = teamPersistence.reload(team);

        Page<ClientLocation> page = null;
        Pageable pageable = null;
        do {
            if (page != null) {
                pageable = page.nextPageable();
            }
            // Find a page of ClientLocation to use
            page = clientLocationPersistence.findByClient(team.getClient()
                    .getId(), pageable);

            if (page.hasContent()) {
                for (ClientLocation clientLocation : page.getContent()) {
                    // if location id is not in exclusion set
                    if (!excludeSet.contains(clientLocation.getLocation()
                            .getId())) {
                        // TeamLocation association does not exist
                        if (teamLocationPersistence.findByTeamAndLocation(team
                                .getId(), clientLocation.getLocation().getId()) == null) {
                            Location location = locationService
                                    .reload(new Location(clientLocation
                                            .getLocation().getId()));
                            // Associate Team with Location
                            added.add(teamLocationPersistence
                                    .saveTeamLocation(new TeamLocation(
                                            location, team)));
                        }
                    }
                }
            }
        } while (page.hasNext());
        return added;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamLocation> findPossibleClientLocationsToAdd(Team team,
                                                               Pageable pageable) {
        Team toFindPossible = teamPersistence.reload(team);
        if (toFindPossible == null) {
            throw new InvalidDataAccessApiUsageException("Team cannot be null");
        }

        // find matching ClientLocations
        Page<ClientLocation> potentials = clientLocationPersistence
                .findByClient(toFindPossible.getClient().getId(), pageable);

        // find TeamLocations for the page of ClientLocations
        List<TeamLocation> teamLocations = new ArrayList<>();
        for (ClientLocation potentialClientLocation : potentials) {
            TeamLocation foundTeamLocation = teamLocationPersistence
                    .findByTeamAndLocation(toFindPossible.getId(),
                            potentialClientLocation.getLocation().getId());
            if (foundTeamLocation != null) {
                teamLocations.add(foundTeamLocation);
            } else {
                teamLocations.add(new TeamLocation(potentialClientLocation
                        .getLocation(), null));
            }
        }
        return new PageImpl<>(teamLocations, pageable,
                potentials.getTotalElements());
    }

}
