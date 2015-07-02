package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamSchedulingConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.DefaultClientTeamSchedulingConfigurationSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.DefaultClientTeamSchedulingConfigurationRepository;

/**
 * Persistence Implementation for DefaultClientTeamSchedulingConfiguration
 */
@Repository
public class DefaultClientTeamSchedulingConfigurationPersistenceImpl implements
        DefaultClientTeamSchedulingConfigurationPersistence {

    @Resource
    DefaultClientTeamSchedulingConfigurationRepository defaultClientTeamSchedulingConfigurationRepository;

    @Resource
    DefaultClientTeamSchedulingConfigurationSpecifications defaultClientTeamSchedulingConfigurationSpecifications;

    @Override
    public DefaultClientTeamSchedulingConfiguration findActive() {
        return defaultClientTeamSchedulingConfigurationRepository
                .findOne(where(defaultClientTeamSchedulingConfigurationSpecifications
                        .isActive()));
    }

    @Override
    public DefaultClientTeamSchedulingConfiguration reload(Long id) {
        return defaultClientTeamSchedulingConfigurationRepository.findOne(id);
    }

    @Override
    public DefaultClientTeamSchedulingConfiguration saveOrUpdate(
            DefaultClientTeamSchedulingConfiguration defaultClientTeamSchedulingConfiguration) {
        return defaultClientTeamSchedulingConfigurationRepository
                .save(defaultClientTeamSchedulingConfiguration);
    }

}
