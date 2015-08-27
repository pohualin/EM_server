package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for ClientTeamPhoneConfiguration Entities
 */
public interface ClientTeamPhoneConfigurationRepository extends
        JpaRepository<ClientTeamPhoneConfiguration, Long>,
        JpaSpecificationExecutor<ClientTeamPhoneConfiguration> {

    /**
     * Find a ClientTeamPhoneConfiguration with given teamId and
     * page specification
     * 
     * @param teamId
     *            to use
     * @return a ClientTeamPhoneConfiguration
     */
    public ClientTeamPhoneConfiguration findByTeamId(
            Long teamId);
  

}
