package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamLocationTeamProviderSaveRequest;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocationSaveRequest;
import com.emmisolutions.emmimanager.persistence.TeamLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderTeamLocationPersistence;
import com.emmisolutions.emmimanager.service.ProviderService;
import com.emmisolutions.emmimanager.service.TeamProviderService;
import com.emmisolutions.emmimanager.service.TeamProviderTeamLocationService;

/**
 *  Implementation of TeamProviderTeamLocationService
 */
@Service
public class TeamProviderTeamLocationServiceImpl implements TeamProviderTeamLocationService {

	@Resource
	TeamProviderTeamLocationPersistence teamProviderTeamLocationPersistence;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    TeamLocationPersistence teamLocationPersistence;
    
    @Resource
    TeamProviderService teamProviderService;

    @Resource
    ProviderService providerService;
    
	@Override
	public Page<TeamProviderTeamLocation> findByTeamProvider(TeamProvider teamProvider, Pageable page) {
		return teamProviderTeamLocationPersistence.findByTeamProvider(teamProvider, page);
	}

	@Override
	public Page<TeamProviderTeamLocation> findByTeamLocation(TeamLocation teamLocation, Pageable page) {
		return teamProviderTeamLocationPersistence.findByTeamLocation(teamLocation, page);
	}

	@Override
    @Transactional
	public Set<TeamProviderTeamLocation> saveAllTeamProviderTeamLocations(List<TeamProviderTeamLocation> teamProviderTeamLocations) {
		return teamProviderTeamLocationPersistence.saveAll(teamProviderTeamLocations);
	}

	@Override
    @Transactional
	public void removeAllByTeamProvider(TeamProvider teamProvider){
		teamProviderTeamLocationPersistence.removeAllByTeamProvider(teamProvider);
	}

    @Override
    @Transactional
    public void updateTeamProviderTeamLocations(TeamLocation teamLocation, TeamLocationTeamProviderSaveRequest request) {
        if(teamLocation != null && request != null) {

            List<TeamProviderTeamLocation> teamProviderTeamLocationsToSave = new ArrayList<TeamProviderTeamLocation>();

			for (TeamProvider teamProvider : request.getProviders()) {
				TeamProviderTeamLocation tptl = new TeamProviderTeamLocation();
				tptl.setTeamProvider(teamProvider);
				tptl.setTeamLocation(teamLocation);
				teamProviderTeamLocationsToSave.add(tptl);
			}

			if (!teamProviderTeamLocationsToSave.isEmpty()){
				teamProviderTeamLocationPersistence.removeAllByTeamLocation(teamLocation);
				teamProviderTeamLocationPersistence.saveAll(teamProviderTeamLocationsToSave);
			}
        }
    }

    @Override
    @Transactional
    public void updateTeamProviderTeamLocations(TeamProvider teamProvider, TeamProviderTeamLocationSaveRequest request) {

    	if(request.getTeamLocations() != null && !request.getTeamLocations().isEmpty()){
    		List<TeamProviderTeamLocation> teamProviderTeamLocationsToSave = new ArrayList<TeamProviderTeamLocation>();

    		for (TeamLocation teamLocation : request.getTeamLocations()) {
    			TeamProviderTeamLocation tptl = new TeamProviderTeamLocation();
    			tptl.setTeamProvider(teamProvider);
    			tptl.setTeamLocation(teamLocation);
    			teamProviderTeamLocationsToSave.add(tptl);
    		}

			teamProviderTeamLocationPersistence.removeAllByTeamProvider(teamProvider);
			teamProviderTeamLocationPersistence.saveAll(teamProviderTeamLocationsToSave);
    	} else {
    		teamProviderTeamLocationPersistence.removeAllByTeamProvider(teamProvider);
    	}
    }
    
    @Override
    @Transactional
	public Set<TeamProviderTeamLocation> createTeamProviderTeamLocation(Set<TeamLocation> teamLocations, TeamProvider teamProvider){

    	TeamProvider teamProviderFromDb = teamProviderService.reload(teamProvider);
    	List<TeamProviderTeamLocation> teamProviderTeamLocationsToSave = new ArrayList<TeamProviderTeamLocation>();
    	
    	for (TeamLocation teamLocation : teamLocations) {
			TeamProviderTeamLocation tptl = new TeamProviderTeamLocation();
			tptl.setTeamProvider(teamProviderFromDb);
			tptl.setTeamLocation(teamLocation);
			teamProviderTeamLocationsToSave.add(tptl);
		}
    	
		return teamProviderTeamLocationPersistence.saveAll(teamProviderTeamLocationsToSave);
    }
}
