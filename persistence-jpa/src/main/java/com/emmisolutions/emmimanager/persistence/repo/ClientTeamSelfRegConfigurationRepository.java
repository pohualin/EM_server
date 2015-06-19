package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for ClientTeamSelfRegConfiguration Entities
 */
public interface ClientTeamSelfRegConfigurationRepository extends
        JpaRepository<ClientTeamSelfRegConfiguration, Long>,
        JpaSpecificationExecutor<ClientTeamSelfRegConfiguration> {

    /**
     * Find a ClientTeamSelfRegConfiguration with given teamId
     *
     * @param teamId to use
     * @return a ClientTeamSelfRegConfiguration
     */
    ClientTeamSelfRegConfiguration findByTeamId(
            Long teamId);


    ClientTeamSelfRegConfiguration findByCode(String code);
}

