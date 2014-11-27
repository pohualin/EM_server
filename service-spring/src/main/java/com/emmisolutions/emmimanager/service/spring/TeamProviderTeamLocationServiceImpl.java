package com.emmisolutions.emmimanager.service.spring;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.persistence.TeamProviderTeamLocationPersistence;
import com.emmisolutions.emmimanager.service.TeamProviderTeamLocationService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamProviderTeamLocationServiceImpl implements TeamProviderTeamLocationService {

	@Resource
	TeamProviderTeamLocationPersistence teamProviderTeamLocationPersistence;

	@Override
	public Page<TeamProviderTeamLocation> findByTeamProvider(TeamProvider teamProvider, Pageable page) {
		return teamProviderTeamLocationPersistence.findByTeamProvider(teamProvider, page);
	}

	@Override
    @Transactional
	public List<TeamProviderTeamLocation> saveAllTeamProviderTeamLocations(List<TeamProviderTeamLocation> teamProviderTeamLocations) {
		return teamProviderTeamLocationPersistence.saveAll(teamProviderTeamLocations);
	}
	
	@Override
	@Transactional
	public void deleteTeamProviderTeamLocations(List<TeamProviderTeamLocation> tptls){
		teamProviderTeamLocationPersistence.delete(tptls);
	}

	@Override
    @Transactional
	public void removeAllByTeamProvider(TeamProvider teamProvider){
		teamProviderTeamLocationPersistence.removeAllByTeamProvider(teamProvider);
	}
}
