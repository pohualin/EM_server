package com.emmisolutions.emmimanager.persistence.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.persistence.TeamProviderTeamLocationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.TeamProviderTeamLocationRepository;

/**
 * TeamProviderTeamLocation Persistence Implementation
 */
@Repository
public class TeamProviderTeamLocationPersistenceImpl implements TeamProviderTeamLocationPersistence {

    @Resource
    TeamProviderTeamLocationRepository teamProviderTeamLocationRepository;

    @Override
    public Set<TeamProviderTeamLocation> saveAll(List<TeamProviderTeamLocation> teamProviderteamLocations) {
        return new HashSet<>(teamProviderTeamLocationRepository.save(teamProviderteamLocations));
    }
    
    @Override
    public void delete(List<TeamProviderTeamLocation> teamProviderTeamLocations){
    	teamProviderTeamLocationRepository.delete(teamProviderTeamLocations);
    }

    @Override
    public Page<TeamProviderTeamLocation> findByTeamProvider(TeamProvider teamProvider, Pageable page) {
    	Pageable pageToFetch;
        if (page == null) {
        	pageToFetch = new PageRequest(0, 10, Sort.Direction.ASC, "id");
        } else {
        	pageToFetch = page;
        }
        return teamProviderTeamLocationRepository.findByTeamProvider(teamProvider, pageToFetch);
    }
    
    @Override
    public Page<TeamProviderTeamLocation> findByTeamLocation(TeamLocation teamLocation, Pageable page) {
    	Pageable pageToFetch;
        if (page == null) {
        	pageToFetch = new PageRequest(0, 10, Sort.Direction.ASC, "id");
        } else {
        	pageToFetch = page;
        }
        return teamProviderTeamLocationRepository.findByTeamLocation(teamLocation, pageToFetch);
    }

    @Override
    public void removeAllByTeamProvider(TeamProvider teamProvider) {
        teamProviderTeamLocationRepository.deleteAllByTeamProvider(teamProvider);
        teamProviderTeamLocationRepository.flush();
    }

    @Override
    public long removeAllByTeamLocation(TeamLocation teamLocation) {
        long ret = teamProviderTeamLocationRepository.deleteByTeamLocation(teamLocation);
        teamProviderTeamLocationRepository.flush();
        return ret;
    }
    
    @Override
    public void removeAllByClientLocation(Client client, Location location) {
        teamProviderTeamLocationRepository.deleteByTeamProviderTeamClientAndTeamLocationLocation(client, location);
    }

    @Override
    public void removeAllByClientProvider(Client client, Provider provider) {
        teamProviderTeamLocationRepository.deleteByTeamProviderTeamClientAndTeamProviderProvider(client, provider);
    }
}
