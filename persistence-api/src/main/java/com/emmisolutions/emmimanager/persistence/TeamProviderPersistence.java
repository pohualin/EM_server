package com.emmisolutions.emmimanager.persistence;

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

	Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team);

	TeamProvider save(TeamProvider teamProvider);

	void delete(TeamProvider provider);

}
