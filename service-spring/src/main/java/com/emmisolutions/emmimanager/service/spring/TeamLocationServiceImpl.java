package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.persistence.ClientLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.LocationService;
import com.emmisolutions.emmimanager.service.TeamLocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class TeamLocationServiceImpl implements TeamLocationService {

    @Resource
    TeamLocationPersistence teamLocationPersistence;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    LocationService locationService;

    @Resource
    ClientLocationPersistence clientLocationPersistence;
    
    @Override
    public Page<TeamLocation> findAllTeamLocationsWithTeam(Pageable pageable, Team team) {
        return teamLocationPersistence.getAllTeamLocationsForTeam(pageable, team);
    }

    @Override
    public void save(Team team, Set<Location> locationSet) {
        Team teamToFind = teamPersistence.reload(team);
        if(teamToFind != null && locationSet != null) {
            for (Location location : locationSet) {
                TeamLocation teamLocation = new TeamLocation(location, teamToFind);
                teamLocationPersistence.saveTeamLocation(teamLocation);
                // add to client location as well
                clientLocationPersistence.create(location.getId(), teamToFind.getClient().getId());
            }
        }
    }

    @Override
    public TeamLocation reload(TeamLocation teamLocation){
        return teamLocationPersistence.reload(teamLocation);
    }

    @Override
    public void delete(TeamLocation teamLocation){
    	teamLocation = teamLocationPersistence.reload(teamLocation);
    	teamLocationPersistence.deleteTeamLocation(teamLocation);
    }
}
