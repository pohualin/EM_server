package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.Team;

/**
 * Spring data repo for Team objects
 */
public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {

	Team findByNormalizedTeamNameAndClientId(String normalizedTeamName, Long client);

}
