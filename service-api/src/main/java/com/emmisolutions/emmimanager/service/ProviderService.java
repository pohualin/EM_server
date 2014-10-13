package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Provider;
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
	Provider create(Provider provider);
	 /**
     * Updates a given provider
     * @param Provider provider to reload
     * @return Provider
     */
	Provider update(Provider provider);
	 /**
     * Finds all providers for a given team
     * @param Pageable pageble
     * @param Team team for which to find providers
     * @return Page<Provider>
     */
	Page<Provider> findAllProviders(Pageable pageble, Team team);
	 /**
     * Finds all specialties for provider reference data.
     * @param Pageable pageable
     * @return Page<ReferenceTag>
     */
	Page<ReferenceTag> findAllSpecialties(Pageable pageble);

}
