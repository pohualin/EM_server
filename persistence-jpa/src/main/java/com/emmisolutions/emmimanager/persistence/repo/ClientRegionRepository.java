package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ClientRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Client repo.
 */
public interface ClientRegionRepository extends JpaRepository<ClientRegion, Long>, JpaSpecificationExecutor<ClientRegion> {

}
