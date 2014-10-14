package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * @param page specifications
     * @param team to find providers for
     * @return a Page<Provider>
     */
	Page<Provider> findAllProvidersByTeam(Pageable page, Team team);

    /**
     * Finds a page of specialty tags
     * @param page to fetch
     * @return a page of ReferenceTag objects
     */
    Page<ReferenceTag> findAllByGroupTypeName(Pageable page);

    /**
     * Name of specialty Type in database
     */
    String SPECIALTY = "SPECIALTY";
}
