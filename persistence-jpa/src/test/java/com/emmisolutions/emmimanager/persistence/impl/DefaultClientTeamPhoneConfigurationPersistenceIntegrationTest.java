package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamPhoneConfigurationPersistence;

/**
 * Test DefaultPhoneConfigurationPersistence
 */
public class DefaultClientTeamPhoneConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    DefaultClientTeamPhoneConfigurationPersistence defaultTeamPhoneConfigurationPersistence;

    /**
     * Test positive findSystemDefault
     */
    @Test
    public void testFindSystemDefault() {
    	Page<DefaultClientTeamPhoneConfiguration> systemDefault = defaultTeamPhoneConfigurationPersistence
                .findActive(null);
        assertThat("active default phone configuration found", systemDefault, is(notNullValue()));
        assertThat("active should be true", systemDefault.getContent().get(1).isActive(),
                is(true));
    }

    /**
     * Test positive save
     */
    @Test
    public void testSave() {
    	Page<DefaultClientTeamPhoneConfiguration>  systemDefault = defaultTeamPhoneConfigurationPersistence
                .findActive(null);
        systemDefault.getContent().get(1).setActive(true);;
        assertThat("system default should be false", systemDefault.getContent().get(1).isActive(),
                is(true));
    }
    
}
