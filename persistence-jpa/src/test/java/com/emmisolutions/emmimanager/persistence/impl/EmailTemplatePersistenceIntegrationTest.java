package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.mail.EmailTemplateType;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import com.emmisolutions.emmimanager.persistence.repo.EmailTemplateRepository;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test EmailTemplate persistence
 */
public class EmailTemplatePersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    EmailTemplatePersistence emailTemplatePersistence;

    @Resource
    EmailTemplateRepository emailTemplateRepository;

    /**
     * Makes sure the database is set up properly
     */
    @Test
    public void hookedUp() {
        assertThat("we are able to attempt to find a template",
                emailTemplatePersistence.find(EmailTemplateType.ACTIVATION),
                is(notNullValue()));
    }

    /**
     * Load a bad type
     */
    @Test
    public void bad() {
        assertThat("null type should not find anything", emailTemplatePersistence.find(null),
                is(nullValue()));
    }

}
