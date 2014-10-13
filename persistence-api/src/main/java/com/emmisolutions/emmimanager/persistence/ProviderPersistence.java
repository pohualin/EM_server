package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;

/**
 * Provider persistence class
 */
public interface ProviderPersistence {
	
    /**
     * Saves a provider
     * @param Provider - to be saved
     * @return the saved provider
     */
	Provider save(Provider provider);
	
    /**
     * Reloads a provider
     * @param Long id - of the provider to load
     * @return Provider provider
     */
	Provider reload(Long id);
	
    /**
     * Finds all providers for a given team
     * @param Pageable
     * @param Team
     * @return Page<Provider>
     */
	Page<Provider> findAllProvidersByTeam(Pageable pageble, Team team);

}
