package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.*;
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
     * @param team    to save
     * @param request locations to associate to the team and for each locations the providers selected
     * @return the saved TPTLs
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

    /**
     * Associate all locations associated with client to the team we passed in.
     * Locations with id contained in excludeSet will be excluded.
     *
     * @param team       to associated to
     * @param excludeSet contains a set of location id to be excluded
     * @return a set of associated TeamLocations
     */
    Set<TeamLocation> associateAllClientLocationsExcept(Team team,
                                                        Set<Long> excludeSet);

    /**
     * Find possible locations to be added to team. Possible locations are
     * locations associated to the team's client
     *
     * @param team     to find
     * @param pageable to use
     * @return a page of potential TeamLocations
     */
    Page<TeamLocation> findPossibleClientLocationsToAdd(Team team,
                                                        Pageable pageable);

}
