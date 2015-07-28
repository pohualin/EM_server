package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data Repo for ScheduledProgram objects that overrides methods form the original
 * repository
 */
public interface ScheduledProgramExtensionRepository extends JpaRepository<ScheduledProgram, Long>,
        JpaSpecificationExecutor<ScheduledProgram> {

    @EntityGraph("scheduledProgramsWithCreatedBy")
    Page<ScheduledProgram> findAll(Specification<ScheduledProgram> spec, Pageable pageable);

}
