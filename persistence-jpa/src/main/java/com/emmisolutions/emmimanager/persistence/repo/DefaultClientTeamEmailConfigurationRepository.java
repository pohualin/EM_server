package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
 * Default Client Team EmailConfiguration repo
 *
 */
public interface DefaultClientTeamEmailConfigurationRepository extends
        JpaRepository<DefaultClientTeamEmailConfiguration, Long>,
        JpaSpecificationExecutor<DefaultClientTeamEmailConfiguration> {

    /**
     * Find all active DefaultClientTeamEmailConfiguration
     *
     * @param specification should be active
     * @param pageable page specification
     * @return an EmailTemplate object or null
     */
    //@Override
    //DefaultClientTeamEmailConfiguration findOne(Specification<DefaultClientTeamEmailConfiguration> specification);

}
