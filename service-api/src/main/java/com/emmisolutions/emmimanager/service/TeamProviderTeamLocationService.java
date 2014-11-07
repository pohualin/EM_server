package com.emmisolutions.emmimanager.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;

/**
 * TeamProviderTeamLocation Service API
 */
@Service
public interface TeamProviderTeamLocationService {
	/**
	 * finds a page of TeamProviderTeamLocations for a given TeamProvider
	 * @param teamProvider
	 * @param pageable
	 * @return
	 */
    Page<TeamProviderTeamLocation> findByTeamProvider(TeamProvider teamProvider, Pageable pageable);
	
	/**
	 * Saves a list if team provider team locations
	 * @param list of teamProviderTeamLocations
	 * @return list of teamProviderTeamLocations
	 */
	List<TeamProviderTeamLocation> saveAllTeamProviderTeamLocations(List<TeamProviderTeamLocation> tptls);

	void removeAllByTeamProvider(TeamProvider teamProvider);

}
