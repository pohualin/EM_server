package com.emmisolutions.emmimanager.persistence;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;

/**
 * TeamProviderTeamLocation persistence class
 */
public interface TeamProviderTeamLocationPersistence {
	
	/**
	 * Saves a list if team provider team locations
	 * @param list of teamProviderTeamLocations
	 * @return list of teamProviderTeamLocations
	 */
	List<TeamProviderTeamLocation> saveAll(List<TeamProviderTeamLocation> teamProviderTeamLocations);
	
	/**
	 * finds a page of TeamProviderTeamLocations for a given TeamProvider
	 * @param teamProvider
	 * @param pageable
	 * @return
	 */
    Page<TeamProviderTeamLocation> findByTeamProvider(TeamProvider teamProvider, Pageable pageable);
 void removeAllByTeamProvider(TeamProvider teamProvider);
}
