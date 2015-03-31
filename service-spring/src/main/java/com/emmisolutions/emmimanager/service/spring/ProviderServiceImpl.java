package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.emmisolutions.emmimanager.model.*;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.persistence.ClientProviderPersistence;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.service.TeamProviderTeamLocationService;
import com.emmisolutions.emmimanager.service.TeamService;

/**
 * Implementation of the ProviderService
 */
@Service
public class ProviderServiceImpl implements ProviderService {

    @Resource
    ProviderPersistence providerPersistence;

    @Resource
    TeamService teamService;

	@Resource
	TeamProviderPersistence teamProviderPersistence;

    @Resource
    ClientService clientService;

    @Resource
    ClientProviderPersistence clientProviderPersistence;
    
    @Resource
    TeamProviderTeamLocationService teamProviderTeamLocationService;

    @Override
    @Transactional(readOnly = true)
    public Provider reload(Provider provider) {
        if (provider == null || provider.getId() == null) {
            return null;
        }
        return providerPersistence.reload(provider);
    }

    @Override
    @Transactional
    public Provider create(Provider provider, Team team) {
        final Team toFind = teamService.reload(team);
        if (provider == null || toFind == null) {
            throw new IllegalArgumentException("provider or team cannot be null");
        }
        provider.setId(null);
        provider.setVersion(null);
		Provider savedProvider =  providerPersistence.save(provider);

        // create the team provider
		TeamProvider teamProvider = new TeamProvider();
		teamProvider.setTeam(toFind);
		teamProvider.setProvider(savedProvider);
		teamProviderPersistence.save(teamProvider);

        // create the client provider
        clientProviderPersistence.create(savedProvider.getId(), toFind.getClient().getId());

		return savedProvider;
    }

    @Override
    @Transactional
    public Provider update(Provider provider) {
        if (provider == null || provider.getId() == null
                || provider.getVersion() == null) {
            throw new IllegalArgumentException(
                    "provider Id and Version cannot be null.");
        }
        return providerPersistence.save(provider);
    }

    @Override
    public Page<ProviderSpecialty> findAllSpecialties(Pageable page) {
    	Pageable pageToFetch;
        if (page == null) {
        	pageToFetch = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        } else {
        	pageToFetch = page;
        }
        return providerPersistence.findAllProviderSpecialties(pageToFetch);
    }

    @Override
    @Transactional(readOnly = true)
	public Page<Provider> list(Pageable page, ProviderSearchFilter filter) {
	        return providerPersistence.list(page, filter);
 	}

    @Override
    @Transactional
    public Provider create(Provider provider) {
        if (provider == null) {
            throw new InvalidDataAccessApiUsageException("provider cannot be null");
        }
        provider.setId(null);
        provider.setVersion(null);
        provider.setActive(true);
        return providerPersistence.save(provider);
    }

    @Override
    @Transactional
    public ProviderSpecialty saveSpecialty(ProviderSpecialty provider) {
        return providerPersistence.saveSpecialty(provider);
    }
}
