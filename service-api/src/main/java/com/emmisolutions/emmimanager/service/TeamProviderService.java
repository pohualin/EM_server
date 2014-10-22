package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;

public interface TeamProviderService {
	 /**
     * Reloads a given provider
     * @param Provider provider to reload
     * @return Provider
     */
	TeamProvider reload(TeamProvider provider);
	
	void deleteProviderFromTeamProvider(Provider provider, Team team);

	Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team);
	
	TeamProvider findByProviderAndTeam(Provider provider, Team team);

}
