package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring data repository for Patient
 */
public interface PatientRepository extends JpaRepository<Patient, Long> , JpaSpecificationExecutor<Patient> {
}
