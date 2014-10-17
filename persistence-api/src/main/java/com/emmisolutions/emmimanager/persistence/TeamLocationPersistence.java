package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
/**
 * TeamTag persistence class
 */
public interface TeamLocationPersistence {
    /**
     * Saves a TeamLocation
     * @param TeamLocation 	to save
     * @return saved TeamLocation
     */
    TeamLocation saveTeamLocation(TeamLocation location);

    /**
     * Deletes a TeamLocation
     * @param TeamLocation 	to delete
     */
    void deleteTeamLocation(TeamLocation location);

    /**
     * Reloads a TeamTag by given ID
     * @param Long id	of the Tag
     * @return Tag
     */
    TeamLocation reload(TeamLocation location);

    /**
     * Gets all the locations associated with a given team
     * @param TeamTag that contains team	to search for
     * @return Page of associated tags for given team
     */
    Page<TeamLocation> getAllTeamLocationsForTeam(Pageable pageable,Team team);

    /**
     * deletes all teamLocations with the given team
     * @param Team team	to search for
     */
    void deleteTeamLocationsWithTeam(Team team);
}
