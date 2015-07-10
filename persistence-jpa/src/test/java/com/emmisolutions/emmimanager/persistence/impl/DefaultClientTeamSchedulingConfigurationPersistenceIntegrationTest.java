package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamSchedulingConfigurationPersistence;

/**
 * Test DefaultClientTeamSchedulingConfigurationPersistence
 */
public class DefaultClientTeamSchedulingConfigurationPersistenceIntegrationTest
        extends BaseIntegrationTest {

    @Resource
    DefaultClientTeamSchedulingConfigurationPersistence defaultClientTeamSchedulingConfigurationPersistence;

    /**
     * Test positive findSystemDefault
     */
    @Test
    public void testFindSystemDefault() {
        DefaultClientTeamSchedulingConfiguration systemDefault = defaultClientTeamSchedulingConfigurationPersistence
                .findActive();
        assertThat(
                "active default client team schedulingdefaultPasswordConfiguration configuration found",
                systemDefault, is(notNullValue()));
        assertThat("active should be true", systemDefault.isActive(), is(true));
    }

    /**
     * Test negative reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadReload() {
        assertThat("bad reload return null",
                defaultClientTeamSchedulingConfigurationPersistence
                        .reload(null), is(nullValue()));
    }

    /**
     * Test positive reload
     */
    @Test
    public void testReload() {
        assertThat("reload works",
                defaultClientTeamSchedulingConfigurationPersistence.reload(1l),
                is(notNullValue()));

        assertThat("reload nothing works",
                defaultClientTeamSchedulingConfigurationPersistence.reload(2l),
                is(nullValue()));
    }

    /**
     * Test positive save
     */
    @Test
    public void testSave() {
        DefaultClientTeamSchedulingConfiguration systemDefault = defaultClientTeamSchedulingConfigurationPersistence
                .findActive();
        systemDefault.setActive(false);
        defaultClientTeamSchedulingConfigurationPersistence
                .saveOrUpdate(systemDefault);
        assertThat("system default should be false", systemDefault.isActive(),
                is(false));
    }
}
