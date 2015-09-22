package com.emmisolutions.emmimanager.service.spring.mail;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.mail.PatientMailService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Patient Mail service implementation
 */
@Service
public class PatientEmailServiceImpl implements PatientMailService {

    @Async
    @Override
    @Transactional
    public void sendReminderEmail(ScheduledProgram scheduledProgram) {
        System.out.println("Send email!!!");

    }
}
