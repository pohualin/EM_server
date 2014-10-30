package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProviderService {
    /**
     * Reloads a given provider
     *
     * @param provider provider to reload
     * @return Provider
     */
    Provider reload(Provider provider);

    /**
     * Create a new provider
     *
     * @param provider the provider
     * @param team     for the team
     * @return the saved provider
     */
    Provider create(Provider provider, Team team);

    /**
     * Updates a given provider
     *
     * @param provider to reload
     * @return the provider
     */
    Provider update(Provider provider);

    /**
     * Finds all specialties for provider reference data.
     *
     * @param pageable the page spec
     * @return a Page of ReferenceTag objects
     */
    Page<ReferenceTag> findAllSpecialties(Pageable pageble);

    /**
     * Finds all providers for given search filter
     *
     * @param page   the page spec
     * @param filter the filter
     * @return list of providers found
     */
    Page<Provider> list(Pageable page, ProviderSearchFilter filter);
}
