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
	 * @param page
	 * @param team
	 * @return page of team-providers
	 */
	Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team);

	/**
	 * Deletes a team-provider
	 *
	 * @param  teamProvider
	 * @return void
	 */
	void delete(TeamProvider provider);
	/**
	 * Associates a list of existing providers to the team passed in
	 *
	 * @param providers
	 * @param team
	 * @return
	 */
	Set<TeamProvider> associateProvidersToTeam(List<TeamProviderTeamLocationSaveRequest> providers, Team team);


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
     * Delete all TeamProviders for a client on the provider of the clientProvider
     *
     * @param client   the client
     * @param provider the provider
     */
    long delete(Client client, Provider provider);
    
    /**
     * Finds a full list of TeamProvider objects for a single team for a
     * discrete list of providers
     *
     * @param teamId  to narrow by
     * @param providers the discrete list of providers we are interested in
     * @return a List of TeamProvider objects
     */
    Page<TeamProvider> findPossibleProvidersToAdd(Team team, ProviderSearchFilter providerSearchFilter, Pageable pageable);

}
