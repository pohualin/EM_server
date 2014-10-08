package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ClientTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Client repo.
 */
public interface ClientTierRepository extends JpaRepository<ClientTier, Long>, JpaSpecificationExecutor<ClientTier> {

}
