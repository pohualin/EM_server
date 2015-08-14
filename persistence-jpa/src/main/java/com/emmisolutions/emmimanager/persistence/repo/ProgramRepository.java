package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.program.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * A Program spring data repository
 */
public interface ProgramRepository extends JpaRepository<Program, Integer>, JpaSpecificationExecutor<Program> {
}
