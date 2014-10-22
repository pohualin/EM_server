package com.emmisolutions.emmimanager.service.spring;

import java.util.HashSet;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.service.TeamService;

@Service
public class ProviderServiceImpl implements ProviderService {

    @Resource
    ProviderPersistence providerPersistence;

    @Resource
    TeamService teamService;
    
	@Resource
	TeamProviderPersistence teamProviderPersistence;

    @Override
    @Transactional(readOnly = true)
    public Provider reload(Provider provider) {
        if (provider == null || provider.getId() == null) {
            return null;
        }
        return providerPersistence.reload(provider.getId());
    }

/*    @Override
    @Transactional
    public Provider create(Provider provider, Team team) {
        final Team toFind = teamService.reload(team);
        if (provider == null || toFind == null) {
            throw new IllegalArgumentException("provider or team cannot be null");
        }
        provider.setId(null);
        provider.setVersion(null);
        provider.setTeams(new HashSet<Team>(){{
            add(toFind);
        }});
        return providerPersistence.save(provider);
    }*/

    @Override
    @Transactional
    public Provider create(Provider provider, Team team) {
        final Team toFind = teamService.reload(team);
        if (provider == null || toFind == null) {
            throw new IllegalArgumentException("provider or team cannot be null");
        }
        provider.setId(null);
        provider.setVersion(null);
        provider.setTeams(new HashSet<Team>(){{
            add(toFind);
        }});
        
		Provider savedProvider =  providerPersistence.save(provider);

		TeamProvider teamProvider = new TeamProvider();
		teamProvider.setTeam(toFind);
		teamProvider.setProvider(savedProvider);
		teamProviderPersistence.save(teamProvider);
			
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
    @Transactional
    public Page<ReferenceTag> findAllSpecialties(Pageable page) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return providerPersistence.findAllByGroupTypeName(page);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Provider> findAllProviders(Pageable page, Team team) {
        Team fromDb = teamService.reload(team);
        if (fromDb == null) {
            throw new IllegalArgumentException("Team not found");
        }
        return providerPersistence.findAllProvidersByTeam(page, fromDb);
    }
    
    @Override
    @Transactional(readOnly = true)
	public Page<Provider> list(Pageable page, ProviderSearchFilter filter) {
	        return providerPersistence.list(page, filter);
 	}
}
