package com.emmisolutions.emmimanager.service;

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
	List<TeamProvider> associateProvidersToTeam(List<Provider> providers, Team team);

	/**
	 * Saves a list of team-providers
	 *
	 * @param teamproviders
	 * @return
	 */
	List<TeamProvider> saveAll(List<TeamProvider> teamProviders);

}
