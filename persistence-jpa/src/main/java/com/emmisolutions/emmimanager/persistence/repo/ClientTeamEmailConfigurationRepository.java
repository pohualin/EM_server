package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for ClientTeamEmailConfiguration Entities
 */
public interface ClientTeamEmailConfigurationRepository extends
        JpaRepository<ClientTeamEmailConfiguration, Long>,
        JpaSpecificationExecutor<ClientTeamEmailConfiguration> {

    /**
     * Find a ClientTeamEmailConfiguration with given teamId.
     *
     * @param teamId to use
     * @return a ClientTeamEmailConfiguration
     */
    ClientTeamEmailConfiguration findByTeamId(Long teamId);

}
