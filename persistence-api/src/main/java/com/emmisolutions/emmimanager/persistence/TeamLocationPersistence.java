package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * TeamTag persistence class
 */
public interface TeamLocationPersistence {
    /**
     * Saves a TeamLocation
     *
     * @param location to save
     * @return saved TeamLocation
     */
    TeamLocation saveTeamLocation(TeamLocation location);

    /**
     * Deletes a TeamLocation
     *
     * @param location to delete
     */
    void deleteTeamLocation(TeamLocation location);

    /**
     * Reloads a TeamLocation
     *
     * @param location to reload
     * @return a TeamLocation
     */
    TeamLocation reload(TeamLocation location);

    /**
     * Gets all the locations associated with a given team
     *
     * @param pageable the page specification
     * @param team     the team
     * @return Page of associated TeamLocation for given team
     */
    Page<TeamLocation> getAllTeamLocationsForTeam(Pageable pageable, Team team);

    /**
     * Finds a page of TeamLocations for a client and location
     *
     * @param client   the client
     * @param location the location
     * @param page     specification
     * @return page of Teams
     */
    Page<Team> findTeamsBy(Client client, Location location, Pageable page);

    /**
     * Delete all TeamLocations for a client and location
     *
     * @param client   the client
     * @param location the location
     * @return the number deleted
     */
    long delete(Client client, Location location);
    
    /**
     * Find existing TeamLocation by teamId and locationId
     * 
     * @param teamId
     *            to use
     * @param locationId
     *            to use
     * @return null or existing TeamLocation
     */
    TeamLocation findByTeamAndLocation(Long teamId, Long locationId);

}
