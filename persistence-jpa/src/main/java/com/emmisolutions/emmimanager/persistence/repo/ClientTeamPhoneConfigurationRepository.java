package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for ClientTeamPhoneConfiguration Entities
 */
public interface ClientTeamPhoneConfigurationRepository extends
        JpaRepository<ClientTeamPhoneConfiguration, Long>,
        JpaSpecificationExecutor<ClientTeamPhoneConfiguration> {

    /**
     * Find a page of ClientTeamPhoneConfiguration with given teamId and
     * page specification
     * 
     * @param teamId
     *            to use
     * @param pageable
     *            to use
     * @return a page of ClientTeamPhoneConfiguration
     */
    public Page<ClientTeamPhoneConfiguration> findByTeamId(
            Long teamId, Pageable pageable);
  

}
