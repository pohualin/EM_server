package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * TeamLocation Service API
 */
@Service
public interface TeamLocationService {

    /**
     * Returns a Page of TeamLocation that have the given team
     *
     * @param pageable the page spec
     * @param team     the team to find within
     * @return page of TeamLocation objects
     */
    Page<TeamLocation> findAllTeamLocationsWithTeam(Pageable pageable, Team team);

    /**
     * saves an association of a team with a set of locations
     *
     * @param team        to save
     * @param locationSet locations to associate to the team
     */
    void save(Team team, Set<Location> locationSet);

    /**
     * reload a team location
     *
     * @param teamLocation to reload
     * @return the team location
     * @throws java.lang.IllegalArgumentException if the location is not found
     */
    TeamLocation reload(TeamLocation teamLocation);

    /**
     * delete a team location
     *
     * @param teamLocation to delete
     * @throws java.lang.IllegalArgumentException if the location is not found
     */
    void delete(TeamLocation teamLocation);

}
