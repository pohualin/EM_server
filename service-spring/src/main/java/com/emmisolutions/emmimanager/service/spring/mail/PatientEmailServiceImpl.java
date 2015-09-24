package com.emmisolutions.emmimanager.service.spring.mail;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import com.emmisolutions.emmimanager.service.ClientTeamEmailConfigurationService;
import com.emmisolutions.emmimanager.service.ScheduleService;
import com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJobMaintenanceService;
import com.emmisolutions.emmimanager.service.mail.MailService;
import com.emmisolutions.emmimanager.service.mail.PatientMailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.model.mail.EmailTemplateType.SCHEDULED_PROGRAM_PATIENT_REMINDER;
import static com.emmisolutions.emmimanager.service.mail.TrackingService.SIGNATURE_VARIABLE_NAME;

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
    ScheduleService scheduleService;
    private EmailTemplatePersistence emailTemplatePersistence;

    @Override
    @Transactional
    public void sendReminderEmail(ScheduledProgram aScheduledProgram,
                                  ScheduleProgramReminderEmailJobMaintenanceService.ReminderDay day) {

        ScheduledProgram scheduledProgram = scheduleService.reload(aScheduledProgram);

        if (scheduledProgram != null) {
            if (!emailTemplatePersistence.emailAlreadySentToday(
                    SCHEDULED_PROGRAM_PATIENT_REMINDER, scheduledProgram.getPatient()) &&
                    teamConfiguredToSendMail(scheduledProgram, day)) {
                // patient hasn't been emailed today and team allows email for the reminder day

                // retrieve tracking url
                String trackingUrl = "http://get.from/" + SIGNATURE_VARIABLE_NAME;

                // get start emmi url to point to
                String startEmmiUrl = "http://startemmi.com";

                // send email
                mailService.sendPatientScheduledProgramReminderEmail(scheduledProgram,
                        startEmmiUrl, trackingUrl);

            }
        }
    }

    private boolean teamConfiguredToSendMail(ScheduledProgram scheduledProgram,
                                             ScheduleProgramReminderEmailJobMaintenanceService.ReminderDay day) {
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
        return sendMail;
    }

    @Override
    @Resource
    public void setEmailTemplatePersistence(EmailTemplatePersistence emailTemplatePersistence) {
        this.emailTemplatePersistence = emailTemplatePersistence;
    }
}
