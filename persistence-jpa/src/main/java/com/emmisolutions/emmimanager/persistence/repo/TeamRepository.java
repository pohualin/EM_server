package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring data repo for Team objects
 */
public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {

    /**
     * Search for a team within a client by its normalized name
     *
     * @param normalizedTeamName to search for
     * @param client             for a client
     * @return the Team
     */
    Team findByNormalizedTeamNameAndClientId(String normalizedTeamName, Long client);

}
