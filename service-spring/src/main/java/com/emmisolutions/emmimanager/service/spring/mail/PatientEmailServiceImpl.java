package com.emmisolutions.emmimanager.service.spring.mail;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import com.emmisolutions.emmimanager.service.ClientTeamEmailConfigurationService;
import com.emmisolutions.emmimanager.service.ScheduleService;
import com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService;
import com.emmisolutions.emmimanager.service.mail.MailService;
import com.emmisolutions.emmimanager.service.mail.PatientMailService;
import com.emmisolutions.emmimanager.service.mail.TrackingService;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.model.mail.EmailTemplateType.SCHEDULED_PROGRAM_PATIENT_REMINDER;
import static org.joda.time.DateTimeZone.UTC;

/**
 * Patient Mail service implementation
 */
@Service
public class PatientEmailServiceImpl implements PatientMailService {

    @Resource
    MailService mailService;

    @Resource
    ClientTeamEmailConfigurationService clientTeamEmailConfigurationService;

    @Resource
    TrackingService trackingService;

    @Resource
    EmailTemplatePersistence emailTemplatePersistence;

    @Resource
    ScheduleService scheduleService;

    @Async
    @Override
    @Transactional
    public void sendReminderEmail(ScheduledProgram aScheduledProgram,
                                  ScheduleProgramReminderEmailJobMaintenanceService.ReminderDay day) {

        ScheduledProgram scheduledProgram = scheduleService.reload(aScheduledProgram);

        if (scheduledProgram != null) {

            // check that the team has reminders enabled
            boolean sendMail = false;
            ClientTeamEmailConfiguration clientTeamEmailConfiguration =
                    clientTeamEmailConfigurationService.findByTeam(scheduledProgram.getTeam());
            if (day != null) {
                switch (day) {
                    case AT_SCHEDULING:
                        sendMail = true;
                        break;
                    case TWO_DAYS_BEFORE_VIEW_BY_DATE:
                        sendMail = clientTeamEmailConfiguration.getReminderTwoDays();
                        break;
                    case FOUR_DAYS_BEFORE_VIEW_BY_DATE:
                        sendMail = clientTeamEmailConfiguration.getReminderFourDays();
                        break;
                    case SIX_DAYS_BEFORE_VIEW_BY_DATE:
                        sendMail = clientTeamEmailConfiguration.getReminderSixDays();
                        break;
                    case EIGHT_DAYS_BEFORE_VIEW_BY_DATE:
                        sendMail = clientTeamEmailConfiguration.getReminderEightDays();
                        break;
                }
            }

            // if we are to send an email, make sure we haven't already sent one today
            if (sendMail && !emailTemplatePersistence.emailAlreadySentToday(
                    SCHEDULED_PROGRAM_PATIENT_REMINDER, scheduledProgram.getPatient())) {

                // retrieve tracking url
                String trackingUrl = "http://get.from";

                // get start emmi url to point to
                String startEmmiUrl = "http://startemmi.com";

                // send email
                mailService.sendPatientScheduledProgramReminderEmail(scheduledProgram,
                        startEmmiUrl, trackingUrl);
            }
        }
    }
}
