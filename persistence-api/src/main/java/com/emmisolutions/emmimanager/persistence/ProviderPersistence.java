package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Provider persistence class
 */
public interface ProviderPersistence {

    /**
     * Saves a provider
     *
     * @param provider Provider - to be saved
     * @return the saved provider
     */
    Provider save(Provider provider);

    /**
     * Reloads a provider
     *
     * @param provider - provider to load
     * @return Provider provider
     */
    Provider reload(Provider provider);

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
     *
     * @param page to fetch
     * @return a page of ReferenceTag objects
     */
    Page<ReferenceTag> findAllByGroupTypeName(Pageable page);

    /**
     * Name of specialty Type in database
     */
    String SPECIALTY = "SPECIALTY";
}
