package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.SalesForce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

/**
 * Spring data repo for SalesForce entities.
 */
public interface SalesForceRepository extends JpaRepository<SalesForce, Long>, JpaSpecificationExecutor<SalesForce> {

    /**
     * Find SalesForce by a set of account numbers
     *
     * @param accountNumbers to use to find
     * @return a list
     */
    List<SalesForce> findByAccountNumberIn(Set<String> accountNumbers);
}
