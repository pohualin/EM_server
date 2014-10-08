package com.emmisolutions.emmimanager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.Team;

public interface ProviderService {
	
	Provider reload(Provider provider);

	Provider create(Provider provider);

	Provider update(Provider provider);

//	Provider addTeam(Long providerId, Team team);
	
	Page<ReferenceTag> findAllSpecialties(Pageable pageble);

}
