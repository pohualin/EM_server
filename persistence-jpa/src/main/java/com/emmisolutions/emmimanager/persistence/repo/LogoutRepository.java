package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.audit.logout.Logout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Logout repo.
 */
public interface LogoutRepository extends JpaRepository<Logout, Long>, JpaSpecificationExecutor<Logout> {

}
