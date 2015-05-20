package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for ClientTeamEmailConfiguration Entities
 */
public interface ClientTeamEmailConfigurationRepository extends
        JpaRepository<ClientTeamEmailConfiguration, Long>,
        JpaSpecificationExecutor<ClientTeamEmailConfiguration> {

    /**
     * Find a page of ClientTeamEmailConfiguration with given userClientId and
     * userClientTeamRoleId
     * 
     * @param teamId
     *            to use
     * @param pageable
     *            to use
     * @return a page of ClientTeamEmailConfiguration
     */
    public Page<ClientTeamEmailConfiguration> findByTeamId(
            Long teamId, Pageable pageable);
}
