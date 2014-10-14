package com.emmisolutions.emmimanager.service.spring;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceTagRepository;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.service.TeamService;

@Service
public class ProviderServiceImpl implements ProviderService {

	@Resource
	ProviderPersistence providerPersistence;

	@Resource
	TeamService teamService;
	
	@Resource
	ReferenceTagRepository referenceTagRepository;

	public static final String SPECIALTY = "SPECIALTY";

	@Override
	@Transactional(readOnly = true)
	public Provider reload(Provider provider) {
		if (provider == null || provider.getId() == null) {
			return null;
		}
		return providerPersistence.reload(provider.getId());
	}

	@Override
	@Transactional
	public Provider create(Provider provider, Team team) {
		if (provider == null || team == null) {
			throw new IllegalArgumentException("provider and team cannot be null");
		}
		provider.setId(null);
		provider.setVersion(null);
		
		Team toFind = teamService.reload(team);
		Set<Team> teams = new HashSet<Team>();
		teams.add(toFind);
		provider.setTeams(teams);
		return providerPersistence.save(provider);
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

		return referenceTagRepository.findAllByGroupTypeName(SPECIALTY, page);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Provider> findAllProviders(Pageable pageble, Team team) {
		if (team == null) {
			return null;
		}
		team = teamService.reload(team);
		return providerPersistence.findAllProvidersByTeam(pageble, team);
	}
}
