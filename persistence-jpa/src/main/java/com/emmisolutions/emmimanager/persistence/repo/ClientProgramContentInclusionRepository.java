package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repo for ClientProgramContentInclusion Entities
 */
public interface ClientProgramContentInclusionRepository extends
        JpaRepository<ClientProgramContentInclusion, Long>,
        JpaSpecificationExecutor<ClientProgramContentInclusion> {

    /**
     * Find a ClientProgramContentInclusion with given clientId and
     * page specification
     * 
     * @param clientId
     *            to use
     * @param pageable
     *            to use
     * @return a ClientProgramContentInclusion
     */
    Page<ClientProgramContentInclusion> findByClientId(
    		Long clientId, Pageable pageable);

}
