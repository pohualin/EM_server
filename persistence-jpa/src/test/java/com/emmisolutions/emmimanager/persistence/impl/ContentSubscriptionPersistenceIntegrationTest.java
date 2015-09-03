package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
     * Test positive find active content subscription
     */
    @Test
    public void testFindActiveContentSubscription() {
    	Page<ContentSubscription> activeContentSubscritpion = contentSubscriptionPersistence
                .findActive(null);
        assertThat("make sure the page containts content subscriptions", activeContentSubscritpion.hasContent(), is(true));
        assertThat("content subscriptions should be active true", activeContentSubscritpion.getContent().get(1).isActive(),
                is(true));
    }
       
}
