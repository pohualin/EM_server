package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.configuration.DefaultPasswordConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultPasswordConfigurationPersistence;

/**
 * Test DefaultPasswordConfigurationPersistence
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
    	Page<DefaultClientTeamEmailConfiguration> systemDefault = defaultTeamEmailConfigurationPersistence
                .findActive(null);
        assertThat("active default password configuration found", systemDefault, is(notNullValue()));
        assertThat("active should be true", systemDefault.getContent().get(1).isActive(),
                is(true));
    }

    /**
     * Test positive save
     */
    @Test
    public void testSave() {
    	Page<DefaultClientTeamEmailConfiguration>  systemDefault = defaultTeamEmailConfigurationPersistence
                .findActive(null);
        systemDefault.getContent().get(1).setActive(true);;
        assertThat("system default should be false", systemDefault.getContent().get(1).isActive(),
                is(true));
    }
    
    /**
     * Test positive reload
     */
    @Test
    public void testReload() {
        assertThat("reload works",
        		defaultTeamEmailConfigurationPersistence.reload(1l),
                is(notNullValue()));

        assertThat("reload nothing works",
        		defaultTeamEmailConfigurationPersistence.reload(2l),
                is(notNullValue()));
    }

}
