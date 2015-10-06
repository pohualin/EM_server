package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultTeamPrintInstructionConfiguration;
import com.emmisolutions.emmimanager.persistence.DefaultTeamPrintInstructionConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.DefaultTeamPrintInstructionConfigurationSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.DefaultTeamPrintInstructionConfigurationRepository;

/**
 * Persistence Implementation to deal with DefaultPasswordConfiguration
 */
@Repository
public class DefaultTeamPrintInstructionConfigurationPersistenceImpl implements
        DefaultTeamPrintInstructionConfigurationPersistence {

    @Resource
    DefaultTeamPrintInstructionConfigurationRepository defaultTeamPrintInstructionConfigurationRepository;

    @Resource
    DefaultTeamPrintInstructionConfigurationSpecifications defaultTeamPrintInstructionConfigurationSpecifications;

    @Override
    public DefaultTeamPrintInstructionConfiguration findActive() {
        return defaultTeamPrintInstructionConfigurationRepository
                .findOne(where(defaultTeamPrintInstructionConfigurationSpecifications
                        .isActive()));
    }

    @Override
    public DefaultTeamPrintInstructionConfiguration reload(Long id) {
        return defaultTeamPrintInstructionConfigurationRepository.findOne(id);
    }

    @Override
    public DefaultTeamPrintInstructionConfiguration saveOrUpdate(
            DefaultTeamPrintInstructionConfiguration defaultTeamPrintInstructionConfiguration) {
        return defaultTeamPrintInstructionConfigurationRepository
                .save(defaultTeamPrintInstructionConfiguration);
    }

}
