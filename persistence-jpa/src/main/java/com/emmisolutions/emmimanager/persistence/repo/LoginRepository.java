package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.audit.login.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Login repo.
 */
public interface LoginRepository extends JpaRepository<Login, Long>, JpaSpecificationExecutor<Login> {

}
