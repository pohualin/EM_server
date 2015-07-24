package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramSearchFilter;
import com.emmisolutions.emmimanager.persistence.*;
import com.emmisolutions.emmimanager.service.ScheduleService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.service.spring.util.AccessCodeGenerator;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.model.schedule.ScheduledProgramSearchFilter.with;

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

    @Resource(name = "clientUserDetailsService")
    UserDetailsService userDetailsService;

    @Override
    @Transactional(readOnly = true)
    public ScheduledProgram schedule(ScheduledProgram toBeScheduled) {
        ScheduledProgram savedScheduledProgram = null;
        if (toBeScheduled != null) {
            hydrateValidProgram(toBeScheduled);
            validateViewByDate(toBeScheduled);
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
    public Page<ScheduledProgram> findAllByPatient(Patient patient, Pageable page) {
        return patient != null && patient.getId() != null ? find(with().patients(patient), page) : null;
    }

    @Override
    public Page<ScheduledProgram> find(ScheduledProgramSearchFilter filter, Pageable page) {
        return schedulePersistence.find(filter, page);
    }

    @Override
    @Transactional
    public ScheduledProgram update(ScheduledProgram scheduledProgram) {
        ScheduledProgram inDb = reload(scheduledProgram);
        if (inDb == null) {
            throw new InvalidDataAccessApiUsageException("Only can update persistent program");
        }
        if (!inDb.getViewByDate().equals(scheduledProgram.getViewByDate())) {
            // validate view by date if it changes
            validateViewByDate(scheduledProgram);
        }

        // only allow changes to view-by-date and active
        scheduledProgram.setAccessCode(inDb.getAccessCode());
        scheduledProgram.setPatient(inDb.getPatient());
        scheduledProgram.setTeam(inDb.getTeam());
        scheduledProgram.setLocation(inDb.getLocation());
        scheduledProgram.setProvider(inDb.getProvider());
        scheduledProgram.setProgram(inDb.getProgram());

        return schedulePersistence.save(scheduledProgram);
    }

    private void hydrateValidProgram(ScheduledProgram scheduledProgram) {
        scheduledProgram.setTeam(teamPersistence.reload(scheduledProgram.getTeam()));
        scheduledProgram.setPatient(patientPersistence.reload(scheduledProgram.getPatient()));
        if (scheduledProgram.getTeam() == null ||
                scheduledProgram.getTeam().getClient() == null ||
                scheduledProgram.getPatient() == null ||
                !scheduledProgram.getTeam().getClient().equals(scheduledProgram.getPatient().getClient())) {
            throw new InvalidDataAccessApiUsageException("Cannot schedule program for patient and team on different clients.");
        }
        scheduledProgram.setLocation(locationPersistence.reload(scheduledProgram.getLocation()));
        scheduledProgram.setProvider(providerPersistence.reload(scheduledProgram.getProvider()));
    }

    private void validateViewByDate(ScheduledProgram scheduledProgram) {
        if (scheduledProgram.getViewByDate() == null ||
                scheduledProgram.getViewByDate().isBefore(LocalDate.now(DateTimeZone.UTC))) {
            throw new InvalidDataAccessApiUsageException("view-by-date (UTC) >= current date. Current UTC date is: " + LocalDate.now(DateTimeZone.UTC).toString());
        }
    }
}
