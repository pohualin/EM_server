package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.audit.logout.LogoutSource;
import com.emmisolutions.emmimanager.model.audit.logout.LogoutSourceName;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * LogoutSource repo.
 */
public interface LogoutSourceRepository extends JpaRepository<LogoutSource, Long>, JpaSpecificationExecutor<LogoutSource> {

    /**
     * Find the LogoutSource by name
     *
     * @param name to search for
     * @return the LogoutSource or null
     */
    @Cacheable(value = "logoutSourceByName", key = "#p0")
    LogoutSource findByName(LogoutSourceName name);
}
