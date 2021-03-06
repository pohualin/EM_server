package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultTeamPrintInstructionConfiguration;

/**
 * Spring Data Repository for DefaultPasswordConfiguration Entity
 */
public interface DefaultTeamPrintInstructionConfigurationRepository extends
        JpaRepository<DefaultTeamPrintInstructionConfiguration, Long>,
        JpaSpecificationExecutor<DefaultTeamPrintInstructionConfiguration> {

    @Override
    @Cacheable(value = "defaultTeamPrintInstructionConfiguration", key = "#p0")
    DefaultTeamPrintInstructionConfiguration findOne(Specification<DefaultTeamPrintInstructionConfiguration> specification);
}
