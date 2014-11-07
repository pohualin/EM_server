package com.emmisolutions.emmimanager.persistence.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.persistence.TeamProviderTeamLocationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TeamProviderTeamLocationRepository;

/**
 * TeamProviderTeamLocation Persistence Implementation
 *
 */
@Repository
public class TeamProviderTeamLocationPersistenceImpl implements TeamProviderTeamLocationPersistence {

	@Resource
	TeamProviderTeamLocationRepository teamProviderTeamLocationRepository;

	@Override
	@Transactional
	public List<TeamProviderTeamLocation> saveAll(List<TeamProviderTeamLocation> teamProviderteamLocations){
		return teamProviderTeamLocationRepository.save(teamProviderteamLocations);
	}
	
    @Override
    public Page<TeamProviderTeamLocation> findByTeamProvider(TeamProvider teamProvider, Pageable page) {
        if (page == null) {
            // default pagination request if none
        	page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return teamProviderTeamLocationRepository.findByTeamProvider(teamProvider, page);
    }
    
    
    
	@Override
	public void removeAllByTeamProvider(TeamProvider teamProvider){
		teamProviderTeamLocationRepository.removeAllByTeamProvider(teamProvider);
		teamProviderTeamLocationRepository.flush();
	}
}
