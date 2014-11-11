package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TeamLocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/*
 * teamLocation persistence implementation
 */
@Repository
public class TeamLocationPersistenceImpl implements TeamLocationPersistence {
    @Resource
    TeamLocationRepository teamLocationRepository;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    LocationPersistence locationPersistence;

    @Override
    public TeamLocation saveTeamLocation(TeamLocation teamLocation) {
        checkTeamLocationNull(teamLocation);
        Team team = teamPersistence.reload(teamLocation.getTeam());
        Location location = locationPersistence.reload(teamLocation.getLocation());
        if (team == null || location == null) {
            throw new IllegalArgumentException("Team or Location is not in the database");
        }

        return teamLocationRepository.save(teamLocation);
    }

    @Override
    public void deleteTeamLocation(TeamLocation teamLocation) {
        checkTeamLocationNull(teamLocation);
        teamLocationRepository.delete(teamLocation);
    }

    @Override
    public TeamLocation reload(TeamLocation teamLocation) {
        checkTeamLocationNull(teamLocation);
        return teamLocationRepository.findOne(teamLocation.getId());
    }

    @Override
    public Page<TeamLocation> getAllTeamLocationsForTeam(Pageable pageable, Team team) {
        if (pageable == null) {
            // default pagination request if none
            pageable = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return teamLocationRepository.findByTeam(team, pageable);
    }

    @Override
    public Page<Team> findTeamsBy(Client client, Location location, Pageable page) {
        return teamLocationRepository.findTeamsByClientAndLocation(client, location, page);
    }

    @Override
    public long delete(Client client, Location location) {
        return teamLocationRepository.deleteByTeamClientAndLocation(client, location);
    }

    private void checkTeamLocationNull(TeamLocation teamLocation) {
        if (teamLocation == null) {
            throw new IllegalArgumentException("team location is null");
        }
    }

}
