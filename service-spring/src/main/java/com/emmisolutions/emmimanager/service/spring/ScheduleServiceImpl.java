package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.schedule.Encounter;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgramSearchFilter;
import com.emmisolutions.emmimanager.persistence.*;
import com.emmisolutions.emmimanager.service.ClientTeamSchedulingConfigurationService;
import com.emmisolutions.emmimanager.service.ScheduleService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.service.spring.util.AccessCodeGenerator;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
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

    private static final String EXCEPTION_PATIENTS_EMAIL_REQUIRED = "Patient's email is required for the team.";

    private static final String EXCEPTION_PATIENTS_PHONE_REQUIRED = "Patient's phone is required for the team.";

    private static final String EXCEPTION_VIEW_BY_DATE = "view-by-date (UTC) >= current date. Current UTC date is: ";

    private static final String EXCEPTION_PROVIDER_REQUIRED = "Provider is required for the team";

    private static final String EXCEPTION_LOCATION_REQUIRED = "Location is required for the team";

    private static final String EXCEPTION_CANNOT_SCHEDULE_DIFFERING_CLIENTS = "Cannot schedule program for patient and team on different clients.";

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
    
    @Resource
    EncounterPersistence encounterPersistence;
    
    @Resource
    ClientTeamSchedulingConfigurationService teamSchedulingConfigurationService;
    
    @Resource
    ClientTeamPhoneConfigurationPersistence clientTeamPhoneConfigurationPersistence;
    
    @Resource
    ClientTeamEmailConfigurationPersistence clientTeamEmailConfigurationPersistence;

    @Resource(name = "clientUserDetailsService")
    UserDetailsService userDetailsService;

    @Override
    @Transactional
    public ScheduledProgram schedule(ScheduledProgram toBeScheduled) {
        ScheduledProgram savedScheduledProgram = null;

        if (toBeScheduled != null) {
            if (toBeScheduled.getEncounter() == null) {
                Encounter encounter = new Encounter();
                encounter.setEncounterDateTime(LocalDateTime
                        .now(DateTimeZone.UTC));
                toBeScheduled
                        .setEncounter(encounterPersistence.save(encounter));
            }
            hydrateValidProgram(toBeScheduled);
            validateViewByDate(toBeScheduled);
            validatePhone(toBeScheduled);
            validateEmail(toBeScheduled);
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
        scheduledProgram.setEncounter(inDb.getEncounter());

        return schedulePersistence.save(scheduledProgram);
    }

    private void hydrateValidProgram(ScheduledProgram scheduledProgram) {
        scheduledProgram.setTeam(teamPersistence.reload(scheduledProgram.getTeam()));
        scheduledProgram.setPatient(patientPersistence.reload(scheduledProgram.getPatient()));
        scheduledProgram.setLocation(locationPersistence.reload(scheduledProgram.getLocation()));
        scheduledProgram.setProvider(providerPersistence.reload(scheduledProgram.getProvider()));

        if (scheduledProgram.getTeam() == null ||
                scheduledProgram.getTeam().getClient() == null ||
                scheduledProgram.getPatient() == null ||
                !scheduledProgram.getTeam().getClient().equals(scheduledProgram.getPatient().getClient())) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_CANNOT_SCHEDULE_DIFFERING_CLIENTS);
        }

        ClientTeamSchedulingConfiguration schedulingConfig = teamSchedulingConfigurationService.findByTeam(scheduledProgram.getTeam());

        if ((schedulingConfig.isUseLocation()) && (scheduledProgram.getLocation() == null )) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_LOCATION_REQUIRED);
        }

        if ((schedulingConfig.isUseProvider()) && (scheduledProgram.getProvider() == null)) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_PROVIDER_REQUIRED);
        }

    }

    private void validateViewByDate(ScheduledProgram scheduledProgram) {
        if (scheduledProgram.getViewByDate() == null ||
                scheduledProgram.getViewByDate().isBefore(LocalDate.now(DateTimeZone.UTC))) {
            throw new InvalidDataAccessApiUsageException(EXCEPTION_VIEW_BY_DATE + LocalDate.now(DateTimeZone.UTC).toString());
        }
    }
    
    private void validatePhone(ScheduledProgram toBeScheduled) {
        ClientTeamPhoneConfiguration teamPhoneConfigDB = clientTeamPhoneConfigurationPersistence.find(toBeScheduled.getTeam().getId());
        if ((teamPhoneConfigDB != null) && (teamPhoneConfigDB.isRequirePhone())) {
            if (StringUtils.isEmpty(toBeScheduled.getPatient().getPhone())) {
                throw new InvalidDataAccessApiUsageException(EXCEPTION_PATIENTS_PHONE_REQUIRED);
            }
        }
   }

    private void validateEmail(ScheduledProgram toBeScheduled) {
        ClientTeamEmailConfiguration teamEmailConfig = clientTeamEmailConfigurationPersistence.find(toBeScheduled.getTeam().getId());

        if ((teamEmailConfig != null) && (teamEmailConfig.isEmailRequired())) {
            if (StringUtils.isEmpty(toBeScheduled.getPatient().getEmail())) {
                throw new InvalidDataAccessApiUsageException(EXCEPTION_PATIENTS_EMAIL_REQUIRED);
            }
        }
    }
}
