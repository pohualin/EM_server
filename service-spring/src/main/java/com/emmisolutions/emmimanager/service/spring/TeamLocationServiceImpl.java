package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamLocationTeamProviderSaveRequest;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.persistence.ClientLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderTeamLocationPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.LocationService;
import com.emmisolutions.emmimanager.service.TeamLocationService;

@Service
public class TeamLocationServiceImpl implements TeamLocationService {

    @Resource
    TeamLocationPersistence teamLocationPersistence;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    LocationService locationService;

    @Resource
    ClientService clientService;

    @Resource
    ClientLocationPersistence clientLocationPersistence;

    @Resource
    TeamProviderTeamLocationPersistence teamProviderTeamLocationPersistence;

    @Override
    public Page<TeamLocation> findAllTeamLocationsWithTeam(Pageable pageable, Team team) {
        return teamLocationPersistence.getAllTeamLocationsForTeam(pageable, team);
    }

    @Override
    @Transactional
    public List<TeamProviderTeamLocation> save(Team team, Set<TeamLocationTeamProviderSaveRequest> request) {
    	List<TeamProviderTeamLocation> savedTptls = new ArrayList<TeamProviderTeamLocation>();
    	Team teamToFind = teamPersistence.reload(team);
        if(teamToFind != null && request != null) {
            for (TeamLocationTeamProviderSaveRequest req : request) {
            	Location location = req.getLocation();
                TeamLocation teamLocation = new TeamLocation(location, teamToFind);
                teamLocationPersistence.saveTeamLocation(teamLocation);
                
                List<TeamProviderTeamLocation> teamProviderTeamLocationsToSave = new ArrayList<TeamProviderTeamLocation>();
            
				for (TeamProvider teamProvider : req.getProviders()) {
					TeamProviderTeamLocation tptl = new TeamProviderTeamLocation();
					tptl.setTeamProvider(teamProvider);
					tptl.setTeamLocation(teamLocation);
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
}
