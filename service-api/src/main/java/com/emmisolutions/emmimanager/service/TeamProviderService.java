package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;

public interface TeamProviderService {
	 /**
     * Reloads a given teamProvider
     * @param teamProvider to reload
     * @return teamProvider
     */
	TeamProvider reload(TeamProvider teamProvider);
	
	Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team);
	
	void delete(TeamProvider provider);
}
