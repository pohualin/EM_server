package com.emmisolutions.emmimanager.persistence.repo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamSchedulingConfiguration;

/**
 * Spring Data Repository for DefaultClientTeamSchedulingConfiguration Entity
 */
public interface DefaultClientTeamSchedulingConfigurationRepository extends
        JpaRepository<DefaultClientTeamSchedulingConfiguration, Long>,
        JpaSpecificationExecutor<DefaultClientTeamSchedulingConfiguration> {

    @Override
    @Cacheable(value = "defaultClientTeamSchedulingConfiguration", key = "#p0")
    DefaultClientTeamSchedulingConfiguration findOne(
            Specification<DefaultClientTeamSchedulingConfiguration> specification);
}
