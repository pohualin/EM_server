package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
/**
 * TeamProviderTeamLocation repository
 */
public interface TeamProviderTeamLocationRepository extends JpaRepository<TeamProviderTeamLocation, Long>, JpaSpecificationExecutor<TeamProviderTeamLocation>{
	
	/**
	 * finds a page of TeamProviderTeamLocations for a given TeamProvider
	 * @param teamProvider
	 * @param pageable
	 * @return
	 */
	Page<TeamProviderTeamLocation> findByTeamProvider(TeamProvider teamProvider, Pageable page);
	long removeAllByTeamProvider(TeamProvider teamProvider);
}
