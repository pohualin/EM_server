package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.SchedulePersistence;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Tests the Scheduling persistence
 */
public class SchedulePersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    SchedulePersistence schedulePersistence;

    @Test
    public void save(){
        ScheduledProgram scheduledProgram = new ScheduledProgram();

        scheduledProgram.setAccessCode("23759604346");
        scheduledProgram.setViewByDate(LocalDateTime.now());
        scheduledProgram.setLocation(new Location(1l));
        scheduledProgram.setProgram(new Program(1));
        scheduledProgram.setTeam(new Team(1l));
        scheduledProgram.setPatient(new Patient());


        schedulePersistence.save(scheduledProgram);
    }
}
