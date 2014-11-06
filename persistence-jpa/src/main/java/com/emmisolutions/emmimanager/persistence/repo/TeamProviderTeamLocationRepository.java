package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;

public interface TeamProviderTeamLocationRepository extends JpaRepository<TeamProviderTeamLocation, Long>, JpaSpecificationExecutor<TeamProviderTeamLocation>{

	Page<TeamProviderTeamLocation> findByTeamProvider(TeamProvider teamProvider, Pageable page);
}
