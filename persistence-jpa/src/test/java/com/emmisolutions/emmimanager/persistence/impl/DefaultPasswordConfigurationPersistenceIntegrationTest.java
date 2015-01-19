package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.configuration.DefaultPasswordConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.DefaultPasswordConfigurationPersistence;

/**
 * Test DefaultPasswordConfigurationPersistence
 */
public class DefaultPasswordConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    DefaultPasswordConfigurationPersistence defaultPasswordConfigurationPersistence;

    /**
     * Test positive findSystemDefault
     */
    @Test
    public void testFindSystemDefault() {
        DefaultPasswordConfiguration systemDefault = defaultPasswordConfigurationPersistence
                .findSystemDefault();
        assertThat("system default found", systemDefault, is(notNullValue()));
        assertThat("system default should be true",
                systemDefault.isSystemDefault(), is(true));
    }

    /**
     * Test negative reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testBadReload() {
        assertThat("bad reload return null",
                defaultPasswordConfigurationPersistence.reload(null),
                is(nullValue()));
    }

    /**
     * Test positive reload
     */
    @Test
    public void testReload() {
        assertThat("reload works",
                defaultPasswordConfigurationPersistence.reload(1l),
                is(notNullValue()));

        assertThat("reload nothing works",
                defaultPasswordConfigurationPersistence.reload(2l),
                is(nullValue()));
    }

    /**
     * Test positive save
     */
    @Test
    public void testSave() {
        DefaultPasswordConfiguration systemDefault = defaultPasswordConfigurationPersistence
                .findSystemDefault();
        systemDefault.setSystemDefault(false);
        defaultPasswordConfigurationPersistence.saveOrUpdate(systemDefault);
        assertThat("system default should be false",
                systemDefault.isSystemDefault(), is(false));
    }
}
