package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.ProviderSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Provider Specialty Repository
 */
public interface ProviderSpecialtyRepository extends JpaRepository<ProviderSpecialty, Long>, JpaSpecificationExecutor<ProviderSpecialty> {
}
