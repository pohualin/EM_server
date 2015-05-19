package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.PatientPersistence;
import com.emmisolutions.emmimanager.persistence.SchedulePersistence;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.ScheduleService;
import com.emmisolutions.emmimanager.service.spring.util.AccessCodeGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Spring implementation of the schedule service
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Resource
    PatientPersistence patientPersistence;

    @Resource
    LocationPersistence locationPersistence;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    AccessCodeGenerator accessCodeGenerator;

    @Resource
    SchedulePersistence schedulePersistence;

    @Override
    @Transactional
    public ScheduledProgram schedule(ScheduledProgram toBeScheduled) {
        ScheduledProgram savedScheduledProgram = null;
        if (toBeScheduled != null) {
            toBeScheduled.setLocation(locationPersistence.reload(toBeScheduled.getLocation()));
            toBeScheduled.setTeam(teamPersistence.reload(toBeScheduled.getTeam()));
            toBeScheduled.setPatient(patientPersistence.reload(toBeScheduled.getPatient()));
            toBeScheduled.setAccessCode(accessCodeGenerator.next());
            savedScheduledProgram = schedulePersistence.save(toBeScheduled);
        }
        return savedScheduledProgram;
    }
}
