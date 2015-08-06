package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.audit.login.LoginStatus;
import com.emmisolutions.emmimanager.model.audit.login.LoginStatusName;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * LoginStatus repo.
 */
public interface LoginStatusRepository extends JpaRepository<LoginStatus, Long>, JpaSpecificationExecutor<LoginStatus> {

    /**
     * Find the login status by name
     *
     * @param name to find
     * @return the persistent status or null
     */
    @Cacheable(value = "loginStatusByName", key = "#p0")
    LoginStatus findByName(LoginStatusName name);
}
