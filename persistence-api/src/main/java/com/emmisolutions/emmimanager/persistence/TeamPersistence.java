package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Team Persistence class
 */
public interface TeamPersistence {

    /**
     * Fetches a page of Team objects
     *
     * @param page         defines which page or null for the first page
     * @param searchFilter to filter the client list
     * @return a page of client objects
     */
    Page<Team> list(Pageable page, TeamSearchFilter searchFilter);

    /**
     * Saves a team
     *
     * @param team to be saved
     * @return the saved client
     */
    Team save(Team team);

    /**
     * Loads a team
     *
     * @param team to load
     * @return the team or null
     */
    Team reload(Team team);
    
    /**
     * Loads a team
     *
     * @param id to load
     * @return the team or null
     */
    Team reload(Long id);

    /**
     * Returns a team by normalized name and Client ID
     *
     * @param normalizedName String
     * @param clientId       the client
     * @return Team
     */
    Team findByNormalizedTeamNameAndClientId(String normalizedName, Long clientId);

}
