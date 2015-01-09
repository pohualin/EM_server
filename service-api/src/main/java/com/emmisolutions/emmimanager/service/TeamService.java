package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Team Service API
 */
public interface TeamService {

    /**
     * Get the first page (default size) of team objects based upon the filter
     *
     * @param teamSearchFilter or null
     * @return the first page of team objects
     */
    Page<Team> list(TeamSearchFilter teamSearchFilter);

    /**
     * Get a page of team objects.
     *
     * @param page             to retrieve
     * @param teamSearchFilter filtered by
     * @return a page of location objects
     */
    Page<Team> list(Pageable page, TeamSearchFilter teamSearchFilter);

    /**
     * Reloads a Team from persistent storage
     *
     * @param toFind to reload
     * @return the reloaded location
     */
    Team reload(Team toFind);

    /**
     * Creates a new team only
     *
     * @param team to be created
     * @return the new team (with id/version)
     */
    Team create(Team team);

    /**
     * Returns a team by normalized name and Client ID
     *
     * @param normalizedName to find
     * @param clientId       for the client
     * @return Team
     */
    Team findByNormalizedNameAndClientId(String normalizedName, Long clientId);

    /**
     * Updates an existing team
     *
     * @param team to save
     * @return the saved team
     */
    Team update(Team team);
}
