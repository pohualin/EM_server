package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;

/**
 * TeamProvider repository
 */
public interface TeamProviderRepository extends JpaRepository<TeamProvider, Long>, JpaSpecificationExecutor<TeamProvider> {

	/**
	 * Finds all team-providers for a given team
	 * 
	 * @param page
	 * @param team
	 * @return page of team-providers
	 */
	Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team);

}
