package com.emmisolutions.emmimanager.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;

/**
 * TeamProvider persistence class
 */
public interface TeamProviderPersistence {

	/**
	 * Reloads a teamProvider
	 * 
	 * @param  id - of the teamProvider to load
	 * @return teamProvider
	 */
	TeamProvider reload(Long id);
	/**
	 * Finds all team-providers for a given team
	 * 
	 * @param page
	 * @param team
	 * @return page of team-providers
	 */
	Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team);
	/**
	 * Saves a teamProvider
	 * 
	 * @param  teamProvider
	 * @return teamProvider
	 */
	TeamProvider save(TeamProvider teamProvider);
	/**
	 * Deletes a team-provider
	 * 
	 * @param  teamProvider
	 * @return void
	 */
	void delete(TeamProvider provider);
	
	/**
	 * Saves a list of teamProviders
	 * 
	 * @param  teamProviders
	 * @return teamProviders
	 */
	List<TeamProvider> saveAll(List<TeamProvider> teamProvider);
	

}
