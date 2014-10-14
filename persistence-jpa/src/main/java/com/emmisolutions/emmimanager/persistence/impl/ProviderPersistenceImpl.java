package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ProviderRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceTagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Provider Persistence Implementation
 *
 */
@Repository
public class ProviderPersistenceImpl implements ProviderPersistence {

	@Resource
	ProviderRepository providerRepository;

    @Resource
    ReferenceTagRepository referenceTagRepository;

	@Override
	public Provider save(Provider provider) {
		return providerRepository.save(provider);
	}

	@Override
	public Provider reload(Long id) {
		return providerRepository.findOne(id);
	}

	@Override
	public Page<Provider> findAllProvidersByTeam(Pageable page, Team team) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "name");
        }
		return providerRepository.findByTeams(page, team);
	}

    @Override
    public Page<ReferenceTag> findAllByGroupTypeName(Pageable page) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "name");
        }
        return referenceTagRepository.findAllByGroupTypeName(SPECIALTY, page);
    }

}
