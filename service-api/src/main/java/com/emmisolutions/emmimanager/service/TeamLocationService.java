package com.emmisolutions.emmimanager.service;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamLocationTeamProviderSaveRequest;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;

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
     * @param request 	locations to associate to the team and for each locations the providers selected
     */
    Set<TeamProviderTeamLocation> save(Team team, Set<TeamLocationTeamProviderSaveRequest> request);

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

    /**
     * Find a page of teams using both the client and location
     *
     * @param client   used
     * @param location used
     * @param pageable the page spec
     * @return a page of Team
     */
    Page<Team> findTeamsBy(Client client, Location location, Pageable pageable);

    /**
     * Delete all TeamLocations for a client and location
     *
     * @param client   the client
     * @param location the location
     * @return the number deleted
     */
    long delete(Client client, Location location);

}
