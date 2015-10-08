package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultTeamPrintInstructionConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.DefaultTeamPrintInstructionConfigurationPersistence;

/**
 * Test DefaultTeamPrintInstructionConfigurationPersistence
 */
public class DefaultTeamPrintInstructionConfigurationPersistenceIntegrationTest
        extends BaseIntegrationTest {

    @Resource
    DefaultTeamPrintInstructionConfigurationPersistence defaultTeamPrintInstructionConfigurationPersistence;

    /**
     * Test positive findSystemDefault
     */
    @Test
    public void testFindSystemDefault() {
        DefaultTeamPrintInstructionConfiguration systemDefault = defaultTeamPrintInstructionConfigurationPersistence
                .findActive();
        assertThat("active default password configuration found",
                systemDefault, is(notNullValue()));
        assertThat("active should be true", systemDefault.isActive(), is(true));
    }

    /**
     * Test negative reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadReload() {
        assertThat("bad reload return null",
                defaultTeamPrintInstructionConfigurationPersistence
                        .reload(null), is(nullValue()));
    }

    /**
     * Test positive reload
     */
    @Test
    public void testReload() {
        assertThat("reload works",
                defaultTeamPrintInstructionConfigurationPersistence.reload(1l),
                is(notNullValue()));

        assertThat("reload nothing works",
                defaultTeamPrintInstructionConfigurationPersistence.reload(2l),
                is(nullValue()));
    }

    /**
     * Test positive save
     */
    @Test
    public void testSave() {
        DefaultTeamPrintInstructionConfiguration systemDefault = defaultTeamPrintInstructionConfigurationPersistence
                .findActive();
        systemDefault.setActive(false);
        defaultTeamPrintInstructionConfigurationPersistence
                .saveOrUpdate(systemDefault);
        assertThat("system default should be false", systemDefault.isActive(),
                is(false));
    }
}
