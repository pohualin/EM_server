package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.persistence.*;
import com.emmisolutions.emmimanager.service.ScheduleService;
import com.emmisolutions.emmimanager.service.spring.util.AccessCodeGenerator;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    ProviderPersistence providerPersistence;

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
            if (toBeScheduled.getViewByDate() == null ||
                    toBeScheduled.getViewByDate().isBefore(LocalDate.now(DateTimeZone.UTC))) {
                throw new InvalidDataAccessApiUsageException("view-by-date (UTC) >= current date. Current UTC date is: " + LocalDate.now(DateTimeZone.UTC).toString());
            }
            toBeScheduled.setTeam(teamPersistence.reload(toBeScheduled.getTeam()));
            toBeScheduled.setPatient(patientPersistence.reload(toBeScheduled.getPatient()));
            if (toBeScheduled.getTeam() == null ||
                    toBeScheduled.getTeam().getClient() == null ||
                    toBeScheduled.getPatient() == null ||
                    !toBeScheduled.getTeam().getClient().equals(toBeScheduled.getPatient().getClient())){
                throw new InvalidDataAccessApiUsageException("Cannot schedule program for patient and team on different clients.");
            }
            toBeScheduled.setLocation(locationPersistence.reload(toBeScheduled.getLocation()));
            toBeScheduled.setProvider(providerPersistence.reload(toBeScheduled.getProvider()));
            toBeScheduled.setAccessCode(accessCodeGenerator.next());

            savedScheduledProgram = schedulePersistence.save(toBeScheduled);
        }
        return savedScheduledProgram;
    }

    @Override
    public ScheduledProgram reload(ScheduledProgram scheduledProgram) {
        return schedulePersistence.reload(scheduledProgram);
    }

    @Override
    public Page<ScheduledProgram> findAllByPatient(Patient patient, Pageable page){
        return schedulePersistence.findAllByPatient(patientPersistence.reload(patient), page);
    }
}
