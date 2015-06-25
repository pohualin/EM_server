package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for ClientTeamSchedulingConfiguration Entities
 */
public interface ClientTeamSchedulingConfigurationRepository extends
        JpaRepository<ClientTeamSchedulingConfiguration, Long>,
        JpaSpecificationExecutor<ClientTeamSchedulingConfiguration> {

    /**
     * Find a ClientTeamSchedulingConfiguration with given teamId and
     * page specification
     * 
     * @param teamId
     *            to use
     * @return a ClientTeamSchedulingConfiguration
     */
    public ClientTeamSchedulingConfiguration findByTeamId(
            Long teamId);
  

}
