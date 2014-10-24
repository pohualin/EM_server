package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
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
}
