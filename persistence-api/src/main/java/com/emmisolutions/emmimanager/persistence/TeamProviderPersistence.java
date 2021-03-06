package com.emmisolutions.emmimanager.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;

/**
 * TeamProvider persistence class
 */
public interface TeamProviderPersistence {

    /**
     * Reloads a teamProvider
     *
     * @param id - of the teamProvider to load
     * @return teamProvider
     */
    TeamProvider reload(Long id);

    /**
     * Finds all team-providers for a given team
     *
     * @param page the page specification
     * @param team to find
     * @return page of team-providers
     */
    Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team);

    /**
     * Finds the team-provider for a given provider and team
     * @param page the page specification
     * @param provider to find
     * @param team	to find
     * @return
     */
    TeamProvider findTeamProvidersByProviderAndTeam(Pageable page, Provider provider, Team team);

    /**
     * Saves a teamProvider
     *
     * @param teamProvider to save
     * @return teamProvider saved
     */
    TeamProvider save(TeamProvider teamProvider);

    /**
     * Deletes a team-provider
     *
     * @param teamProvider to delete
     */
    void delete(TeamProvider teamProvider);

    /**
     * Saves a list of teamProviders
     *
     * @param teamProviders to save
     * @return teamProviders saved
     */
    List<TeamProvider> saveAll(List<TeamProvider> teamProviders);

    /**
     * Delete all TeamProviders for a client and provider
     *
     * @param client   the client
     * @param provider the provider
     * @return the number deleted
     */
    long delete(Client client, Provider provider);

    /**
     * Find a page of Teamr objects by
     * @param client the client
     * @param provider the provider
     * @param page specification
     * @return a page of TeamProvider objects
     */
    Page<Team> findTeamsBy(Client client, Provider provider, Pageable page);

    /**
     * Finds a full list of TeamProvider objects for a single team for a
     * discrete list of providers
     * 
     * @param teamId		to narrow by
     * @param matchedProviders the discrete list of providers we are interested in
     * @return
     */
    List<TeamProvider> getByTeamIdAndProviders(Long teamId, Page<Provider> matchedProviders);
    
    /**
     * Find TeamProvider by team id and provider id
     * 
     * @param teamId
     *            to use
     * @param providerId
     *            to use
     * @return existing record or null
     */
    TeamProvider findTeamProviderByTeamAndProvider(Long teamId, Long providerId);

}
