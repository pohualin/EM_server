package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.program.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * A ProgramSpecialty spring data repository
 */
public interface ProgramSpecialtyRepository extends JpaRepository<Specialty, Long>, JpaSpecificationExecutor<Specialty> {
}
