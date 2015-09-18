package com.emmisolutions.emmimanager.service.spring.jobs;

import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.service.jobs.ScheduleProgramReminderEmailJob;
import org.springframework.stereotype.Service;

/**
 * Stub scheduler
 */
@Service
public class ScheduleProgramReminderEmailJobStub implements ScheduleProgramReminderEmailJob {

    @Override
    public void scheduleReminder(ScheduledProgram scheduledProgram) {
        // no - op
    }
}
