package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.persistence.SchedulePersistence;
import com.emmisolutions.emmimanager.persistence.repo.ScheduledProgramRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Schedule persistence repository
 */
@Repository
public class SchedulePersistenceImpl implements SchedulePersistence {

    @Resource
    ScheduledProgramRepository scheduledProgramRepository;

    @Override
    public boolean isAccessCodeUnique(String toCheck) {
        return scheduledProgramRepository.findFirstByAccessCodeEquals(toCheck) == null;
    }

    @Override
    public ScheduledProgram save(ScheduledProgram toSave) {
        return scheduledProgramRepository.save(toSave);
    }
}
