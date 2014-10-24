package com.emmisolutions.emmimanager.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.ReferenceTag;

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
    * Fetches a page of Provider objects
    *
    * @param page         defines which page or null for the first page
    * @param searchFilter to filter
    * @return a page of provider objects
    */
   Page<Provider> list(Pageable page, ProviderSearchFilter searchFilter);

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
