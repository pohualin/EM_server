package com.emmisolutions.emmimanager.service.spring.mail;

import com.emmisolutions.emmimanager.model.mail.EmailTemplateTracking;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateType;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.mail.TrackingService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Test that the tracking service works for emails
 */
public class TrackingServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    EmailTemplatePersistence emailTemplatePersistence;

    @Resource
    TrackingService trackingService;

    /**
     * Ensure that tracking and view logging works properly
     */
    @Test
    public void viewsAreLogged() {
        UserClient userClient = new UserClient();
        userClient.setEmail("test@test.com");
        EmailTemplateTracking tracked = emailTemplatePersistence.log(
                emailTemplatePersistence.find(EmailTemplateType.PASSWORD_CHANGED), userClient);

        assertThat("tracker has not been viewed", tracked.isViewed(), is(false));
        trackingService.viewed(tracked.getSignature());

        EmailTemplateTracking viewed = emailTemplatePersistence.load(tracked.getSignature());
        assertThat("tracker should be viewed", viewed.isViewed(), is(true));
        assertThat("tracker should have a view by date", viewed.getViewedDate(), is(notNullValue()));

    }
}
