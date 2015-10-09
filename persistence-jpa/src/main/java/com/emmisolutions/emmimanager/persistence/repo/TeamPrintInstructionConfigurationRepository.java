package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.TeamPrintInstructionConfiguration;

/**
 * Spring Data Repository for TeamPrintInstructionConfiguration Entity
 */
public interface TeamPrintInstructionConfigurationRepository extends
        JpaRepository<TeamPrintInstructionConfiguration, Long>,
        JpaSpecificationExecutor<TeamPrintInstructionConfiguration> {

    /**
     * Find TeamPrintInstructionConfiguration by passed in team
     *
     * @param team
     *            to lookup
     * @return null or an existing TeamPrintInstructionConfiguration
     */
    public TeamPrintInstructionConfiguration findByTeam(Team team);

}
