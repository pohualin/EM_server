package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ContentSubscriptionPersistence;

/**
 * Test Content Subscription Persistence
 */
public class ContentSubscriptionPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ContentSubscriptionPersistence contentSubscriptionPersistence;

    /**
     * Test positive findSystemDefault
     */
    @Test
    public void testFindSystemDefault() {
    	Page<ContentSubscription> systemDefault = contentSubscriptionPersistence
                .findActive(null);
        assertThat("active content subscription found", systemDefault, is(notNullValue()));
        assertThat("active should be true", systemDefault.getContent().get(1).isActive(),
                is(true));
    }

    /**
     * Test positive save
     */
    @Test
    public void testSave() {
    	Page<ContentSubscription>  systemDefault = contentSubscriptionPersistence
                .findActive(null);
        systemDefault.getContent().get(1).setActive(true);;
        assertThat("system default should be false", systemDefault.getContent().get(1).isActive(),
                is(true));
    }
    
}
