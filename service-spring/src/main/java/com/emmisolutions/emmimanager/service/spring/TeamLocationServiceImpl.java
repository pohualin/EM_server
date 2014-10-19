package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.ClientLocationModificationRequest;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.persistence.TeamLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
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
    
    @Override
    public Page<TeamLocation> findAllTeamLocationsWithTeam(Pageable pageable, Team team) {
        return teamLocationPersistence.getAllTeamLocationsForTeam(pageable, team);
    }

    @Override
    public void save(Team team, List<Location> locationList) {
        Team teamToFind = teamPersistence.reload(team);
        List<Location> locationToAdd = new ArrayList<Location>();
        if(teamToFind != null && locationList != null) {
            for (Location location : locationList) {
                TeamLocation teamLocation = new TeamLocation(location, teamToFind);
                teamLocationPersistence.saveTeamLocation(teamLocation);
                if (locationService.reloadLocationUsedByClient(teamToFind.getClient(), location) == null) { //check if the location is used by the client
                	//if not used, add to the client
                	Location loca = locationService.reload(location);
                	locationToAdd.add(loca);
                }
            }
        }
        if (locationToAdd.size() > 0) {
	        //after save the locations team, need to add those locations to the client
	        ClientLocationModificationRequest modificationRequest = new ClientLocationModificationRequest();
	        modificationRequest.setAdded(locationToAdd);
	        locationService.updateClientLocations(teamToFind.getClient(), modificationRequest);
        }
    }

    @Override
    public TeamLocation reload(TeamLocation teamLocation){
        return teamLocationPersistence.reload(teamLocation);
    }

}
