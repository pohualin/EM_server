package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.DefaultClientTeamEmailConfigurationSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.DefaultClientTeamEmailConfigurationRepository;

/**
 * Persistence Implementation to deal with DefaultPasswordConfiguration
 */
@Repository
public class DefaultClientTeamEmailConfigurantionPersistenceImpl implements
        DefaultClientTeamEmailConfigurationPersistence {

    @Resource
    DefaultClientTeamEmailConfigurationRepository defaultClientTeamEmailConfigurationRepository;
    
    @Resource
    DefaultClientTeamEmailConfigurationSpecifications defaultClientTeamEmailConfigurationSpecifications;

    @Override
    public DefaultClientTeamEmailConfiguration find() {
        return defaultClientTeamEmailConfigurationRepository.
                findOne(where(defaultClientTeamEmailConfigurationSpecifications.isActive()));
    }

}
