package com.emmisolutions.emmimanager.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;

/**
 * TeamLocation Service API
 */
@Service
public interface TeamLocationService {
    /**
     * Returns a Page of TeamLocation that have the given team
     */
    Page<TeamLocation> findAllTeamLocationsWithTeam(Pageable pageable, Team team);

    /**
     * saves an association of a team with a set of locations
     */
    void save(Team team, Set<Location> locationSet);

    /**
     * reload a team location
     */
    TeamLocation reload(TeamLocation teamLocation);

    /**
     * delete a team location
     * 
     * @param teamLocation
     */
	void delete(TeamLocation teamLocation);
    
}