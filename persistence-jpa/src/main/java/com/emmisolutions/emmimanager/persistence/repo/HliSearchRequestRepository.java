package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.program.hli.HliSearchRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The HliSearchRequest spring-data repository
 */
public interface HliSearchRequestRepository extends
        JpaRepository<HliSearchRequest, Long>,
        JpaSpecificationExecutor<HliSearchRequest> {

}
