package com.emmisolutions.emmimanager.service.spring.mail;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateTracking;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.mail.TrackingService;
import org.junit.Test;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.model.mail.EmailTemplateType.PASSWORD_CHANGED;
import static com.emmisolutions.emmimanager.model.mail.EmailTemplateType.SCHEDULED_PROGRAM_PATIENT_REMINDER;
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
                emailTemplatePersistence.find(PASSWORD_CHANGED), userClient);

        assertThat("tracker has not been viewed", tracked.isViewed(), is(false));
        trackingService.viewed(tracked.getSignature());

        EmailTemplateTracking viewed = emailTemplatePersistence.load(tracked.getSignature());
        assertThat("tracker should be viewed", viewed.isViewed(), is(true));
        assertThat("tracker should have a view by date", viewed.getViewedDate(), is(notNullValue()));

    }

    /**
     * Ensure that tracking plus action logging is proper
     */
    @Test
    public void actionsAreLogged() {
        Patient patient = new Patient();
        patient.setEmail("test_p@test.com");
        EmailTemplateTracking tracked = emailTemplatePersistence.log(
                emailTemplatePersistence.find(SCHEDULED_PROGRAM_PATIENT_REMINDER), patient);

        assertThat("tracker has not been viewed", tracked.isActionTaken(), is(false));
        trackingService.actionTaken(tracked.getSignature());

        EmailTemplateTracking actionTaken = emailTemplatePersistence.load(tracked.getSignature());
        assertThat("tracker should not be viewed", actionTaken.isViewed(), is(false));
        assertThat("tracker should have action taken", actionTaken.isActionTaken(), is(true));
        assertThat("tracker should have an action date", actionTaken.getActionTakenDate(), is(notNullValue()));

    }
}
