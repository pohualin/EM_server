package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramNote;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramSearchFilter;
import com.emmisolutions.emmimanager.persistence.SchedulePersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.ScheduledProgramSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.ScheduledProgramExtensionRepository;
import com.emmisolutions.emmimanager.persistence.repo.ScheduledProgramNotesRepository;
import com.emmisolutions.emmimanager.persistence.repo.ScheduledProgramRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.emmisolutions.emmimanager.model.schedule.ScheduledProgramSearchFilter.with;
import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Schedule persistence repository
 */
@Repository
public class SchedulePersistenceImpl implements SchedulePersistence {

    @Resource
    ScheduledProgramExtensionRepository scheduledProgramExtensionRepository;

    @Resource
    ScheduledProgramRepository scheduledProgramRepository;

    @Resource
    ScheduledProgramNotesRepository scheduledProgramNotesRepository;

    @Resource
    ScheduledProgramSpecifications specifications;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public boolean isAccessCodeUnique(String toCheck) {
        return scheduledProgramRepository.findFirstByAccessCodeEquals(toCheck) == null;
    }

    @Override
    public ScheduledProgram save(ScheduledProgram toSave) {
        return scheduledProgramRepository.save(toSave);
    }

    @Override
    public ScheduledProgram reload(final ScheduledProgram scheduledProgram) {
        if (scheduledProgram == null || scheduledProgram.getId() == null) {
            return null;
        }
        ScheduledProgramSearchFilter searchFilter = with().id(scheduledProgram.getId()).team(scheduledProgram.getTeam());
        return scheduledProgramRepository.findOne(where(
                specifications.id(searchFilter)).and(
                specifications.team(searchFilter)));
    }

    @Override
    public Page<ScheduledProgram> find(ScheduledProgramSearchFilter filter, Pageable page) {
        return scheduledProgramExtensionRepository.findAll(where(specifications.id(filter))
                        .and(specifications.team(filter))
                        .and(specifications.expired(filter))
                        .and(specifications.patients(filter))
                        .and(specifications.accessCodes(filter))
                        .and(specifications.encounter(filter)),
                page == null ? new PageRequest(0, 50, Sort.Direction.ASC, "viewByDate") : page);
    }

    @Override
    public ScheduledProgramNote findNotes(String accessCode) {
        return scheduledProgramNotesRepository.find(accessCode);
    }
}