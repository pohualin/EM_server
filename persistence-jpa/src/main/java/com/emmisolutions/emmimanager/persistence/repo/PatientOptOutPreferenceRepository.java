package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.PatientOptOutPreference;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Client repo.
 */
public interface PatientOptOutPreferenceRepository extends JpaRepository<PatientOptOutPreference, Long>,
        JpaSpecificationExecutor<PatientOptOutPreference> {

    @Override
    @Cacheable(value = "allPossiblePatientOptOutPreferences")
    List<PatientOptOutPreference> findAll();
}
