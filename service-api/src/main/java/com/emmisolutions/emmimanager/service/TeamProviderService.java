package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Team Provider Service API
 */
public interface TeamProviderService {

    /**
     * Reloads a given teamProvider
     *
     * @param teamProvider to reload
     * @return teamProvider
     */
    TeamProvider reload(TeamProvider teamProvider);

    /**
     * Finds all team-providers for a given team
     *
     * @param page the page specification
     * @param team to user
     * @return page of team-providers
     */
    Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team);

    /**
     * Find Teams by Client and Provider
     *
     * @param client   to use
     * @param provider to use
     * @param page the page specification
     * @return a page of TeamProvider objects
     */
    Page<Team> findTeamsBy(Client client, Provider provider, Pageable pageable);


    /**
     * Deletes a team-provider
     *
     * @param teamProvider to delete
     * @return void
     */
    void delete(TeamProvider provider);

    /**
     * Associates a list of existing providers to the team passed in
     *
     * @param providers to associate
     * @param team      to associate to
     * @return list of saved TeamProvider objects
     */
    List<TeamProvider> associateProvidersToTeam(List<Provider> providers, Team team);

    /**
     * Saves a list of team-providers
     *
     * @param teamProviders list of team providers to save
     * @return saved list
     */
    List<TeamProvider> saveAll(List<TeamProvider> teamProviders);

    /**
     * Delete all TeamProviders for a client on the provider of the clientProvider
     *
     * @param client   the client
     * @param provider the provider
     */
    long delete(Client client, Provider provider);
}
