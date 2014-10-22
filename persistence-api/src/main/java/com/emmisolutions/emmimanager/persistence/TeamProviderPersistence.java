package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;

/**
 * Provider persistence class
 */
public interface TeamProviderPersistence {
	
    /**
     * Reloads a provider
     * @param Long id - of the provider to load
     * @return Provider provider
     */
	TeamProvider reload(Long id);
	
	Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team);

	TeamProvider save(TeamProvider teamProvider);

	TeamProvider findByProviderAndTeam(Provider provider, Team team);
}
