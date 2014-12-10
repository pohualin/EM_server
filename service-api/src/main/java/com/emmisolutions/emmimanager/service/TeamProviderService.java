package com.emmisolutions.emmimanager.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocationSaveRequest;

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
     * @param page specification
     * @param team to find for
     * @return page of TeamProvider objects
     */
    Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team);

    /**
     * Deletes a team-provider
     *
     * @param provider to delete
     */
    void delete(TeamProvider provider);

    /**
     * Update a teamProvider
     *
     * @param request to be updated
     */
    void updateTeamProvider(TeamProviderTeamLocationSaveRequest request);

    /**
     * Associates a list of existing providers to the team passed in
     *
     * @param providers to associate
     * @param team      to this team
     * @return the saved set
     */
    Set<TeamProvider> associateProvidersToTeam(List<TeamProviderTeamLocationSaveRequest> providers, Team team);


    /**
     * Find Teams by Client and Provider
     *
     * @param client   to use
     * @param provider to use
     * @param pageable the page specification
     * @return a page of TeamProvider objects
     */
    Page<Team> findTeamsBy(Client client, Provider provider, Pageable pageable);

    /**
     * Delete all TeamProviders for a client on the provider of the clientProvider
     *
     * @param client   the client
     * @param provider the provider
     * @return number deleted
     */
    long delete(Client client, Provider provider);

    /**
     * Find all teamLocation by teamProvider
     *
     * @param teamProvider to use
     * @param pageable     the page specification
     * @return a set of TeamProviderTeamLocation objects
     */
    Page<TeamProviderTeamLocation> findTeamLocationsByTeamProvider(TeamProvider teamProvider, Pageable pageable);
 
    /**
     * Finds a full list of TeamProvider objects for a single team for the provided providerSearchFilter
     *
     * @param team to narrow by
     * @param providerSearchFilter 
     * @param pageable  the page specification
     * @return
     */
    Page<TeamProvider> findPossibleProvidersToAdd(Team team, ProviderSearchFilter providerSearchFilter, Pageable pageable);

}
