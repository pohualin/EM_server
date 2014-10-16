package com.emmisolutions.emmimanager.service.spring;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.persistence.TeamLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.TeamLocationService;

@Service
public class TeamLocationServiceImpl implements TeamLocationService {

    @Resource
    TeamLocationPersistence teamLocationPersistence;

    @Resource
    TeamPersistence teamPersistence;

    @Override
    public Page<TeamLocation> findAllTeamLocationsWithTeam(Pageable pageable, Team team) {
        return teamLocationPersistence.getAllTeamLocationsForTeam(pageable, team);
    }

    @Override
    public void save(Team team, Set<Location> tagSet) {
        Team teamToFind = teamPersistence.reload(team);
        if(teamToFind != null) {
        	//TODO is necessary to delete all previous locations ?
        	//teamLocationPersistence.deleteTeamLocationsWithTeam(teamToFind);
            if (tagSet != null) {
                for (Location location : tagSet) {
                    TeamLocation teamLocation = new TeamLocation(location, teamToFind);
                    teamLocationPersistence.saveTeamLocation(teamLocation);
                }
            }
        }
    }

    @Override
    public TeamLocation reload(TeamLocation teamLocation){
        return teamLocationPersistence.reload(teamLocation);
    }
}
