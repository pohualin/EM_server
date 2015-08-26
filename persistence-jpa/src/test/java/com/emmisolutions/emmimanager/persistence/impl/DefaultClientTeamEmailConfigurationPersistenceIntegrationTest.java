package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamEmailConfigurationPersistence;

/**
 * Test DefaultEmailConfigurationPersistence
 */
public class DefaultClientTeamEmailConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    DefaultClientTeamEmailConfigurationPersistence defaultTeamEmailConfigurationPersistence;

    /**
     * Test positive findSystemDefault
     */
    @Test
    public void testFindSystemDefault() {
        DefaultClientTeamEmailConfiguration systemDefault = defaultTeamEmailConfigurationPersistence.find();
        assertThat("active default email configuration found", systemDefault, is(notNullValue()));
        assertThat("active should be true", systemDefault.isActive(), is(true));
    }

}
