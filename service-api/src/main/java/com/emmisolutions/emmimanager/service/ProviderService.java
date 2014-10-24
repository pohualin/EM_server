package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.Team;

public interface ProviderService {
	 /**
     * Reloads a given provider
     * @param Provider provider to reload
     * @return Provider
     */
	Provider reload(Provider provider);
	 /**
     * Create a new provider
     * @param Provider provider to reload
     * @return Provider
     */
	Provider create(Provider provider, Team team);
	 /**
     * Updates a given provider
     * @param Provider provider to reload
     * @return Provider
     */
	Provider update(Provider provider);
	 /**
     * Finds all specialties for provider reference data.
     * @param Pageable pageable
     * @return Page<ReferenceTag>
     */
	Page<ReferenceTag> findAllSpecialties(Pageable pageble);
	 /**
     * Finds all providers for given search filter
     * @param page
     * @param providerSearchFilter
     * @return list of providers found
     */
	Page<Provider> list(Pageable page, ProviderSearchFilter filter);
}
