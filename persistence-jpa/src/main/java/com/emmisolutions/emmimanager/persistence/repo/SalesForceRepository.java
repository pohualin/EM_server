package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.SalesForce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring data repo for SalesForce entities.
 */
public interface SalesForceRepository extends JpaRepository<SalesForce, Long>, JpaSpecificationExecutor<SalesForce> {
}
