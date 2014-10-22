package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;

public interface TeamProviderRepository extends
		JpaRepository<TeamProvider, Long>,
		JpaSpecificationExecutor<TeamProvider> {

	TeamProvider findByProviderAndTeam(Provider provider, Team team);

	Page<TeamProvider> findTeamProvidersByTeam(Pageable page, Team team);

}
