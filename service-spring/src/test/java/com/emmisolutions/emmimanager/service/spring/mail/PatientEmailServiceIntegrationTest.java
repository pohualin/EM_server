package com.emmisolutions.emmimanager.service.spring.mail;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.mail.PatientMailService;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Test;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.model.mail.EmailTemplateType.SCHEDULED_PROGRAM_PATIENT_REMINDER;
import static com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService.ReminderDay.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests the patient email service
 */
public class PatientEmailServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    PatientMailService patientMailService;

    @Resource
    EmailTemplatePersistence emailTemplatePersistence;

    @Test
    public void full(@Mocked final EmailTemplatePersistence mockETPersistence) {
        final ScheduledProgram scheduledProgram = makeNewScheduledProgram(null);

        // allow for the sending of multiple emails per day just for this test
        new NonStrictExpectations() {{
            mockETPersistence.emailAlreadySentToday(SCHEDULED_PROGRAM_PATIENT_REMINDER, scheduledProgram.getPatient());
            returns(false);
        }};
        assertThat("email has not been sent",
                emailTemplatePersistence.emailAlreadySentToday(SCHEDULED_PROGRAM_PATIENT_REMINDER,
                        scheduledProgram.getPatient()), is(false));

        // send a bunch of emails
        patientMailService.setEmailTemplatePersistence(mockETPersistence);
        patientMailService.sendReminderEmail(scheduledProgram, AT_SCHEDULING);
        patientMailService.sendReminderEmail(scheduledProgram, TWO_DAYS_BEFORE_VIEW_BY_DATE);
        patientMailService.sendReminderEmail(scheduledProgram, FOUR_DAYS_BEFORE_VIEW_BY_DATE);
        patientMailService.sendReminderEmail(scheduledProgram, SIX_DAYS_BEFORE_VIEW_BY_DATE);
        patientMailService.sendReminderEmail(scheduledProgram, EIGHT_DAYS_BEFORE_VIEW_BY_DATE);

        // set the template back for subsequent tests
        patientMailService.setEmailTemplatePersistence(emailTemplatePersistence);

        assertThat("email was sent",
                emailTemplatePersistence.emailAlreadySentToday(SCHEDULED_PROGRAM_PATIENT_REMINDER,
                        scheduledProgram.getPatient()), is(true));

    }

}
