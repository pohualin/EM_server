package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for InfoHeaderConfig Entities
 */
public interface InfoHeaderConfigRepository extends
        JpaRepository<InfoHeaderConfig, Long>,
        JpaSpecificationExecutor<InfoHeaderConfig> {

}

