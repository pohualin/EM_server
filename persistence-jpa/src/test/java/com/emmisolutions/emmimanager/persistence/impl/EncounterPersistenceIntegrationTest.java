package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import com.emmisolutions.emmimanager.model.schedule.Encounter;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.EncounterPersistence;

/**
 * Tests the Encounter persistence
 */
public class EncounterPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    EncounterPersistence encounterPersistence;

    /**
     * Happy path save success.
     */
    @Test
    public void save() {
        Encounter encounter = new Encounter();
        encounter.setEncounterDateTime(LocalDateTime.now(DateTimeZone.UTC));
        encounter = encounterPersistence.save(encounter);
        assertThat("Encounter created", encounter.getId(), is(notNullValue()));

        Encounter reload = encounterPersistence.reload(new Encounter(encounter
                .getId()));
        assertThat("reloaded same instance", reload, is(encounter));
    }

}
