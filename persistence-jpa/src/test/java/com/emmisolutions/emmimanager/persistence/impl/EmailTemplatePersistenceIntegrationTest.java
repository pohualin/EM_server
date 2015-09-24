package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateTracking;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import org.junit.Test;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static com.emmisolutions.emmimanager.model.mail.EmailTemplateType.*;
import static com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence.SERVER_SIDE_SECRET;
import static com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence.encoder;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test EmailTemplate persistence
 */
public class EmailTemplatePersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    EmailTemplatePersistence emailTemplatePersistence;

    /**
     * Makes sure the database is set up properly
     */
    @Test
    public void hookedUp() {
        assertThat("we are able to attempt to find a template",
                emailTemplatePersistence.find(ACTIVATION),
                is(notNullValue()));
    }

    /**
     * Make sure an email sent tracking object is created
     */
    @Test
    public void log() {
        UserClient userClient = new UserClient();
        userClient.setEmail("anEmail@matt.com");
        EmailTemplateTracking tracking =
                emailTemplatePersistence.log(emailTemplatePersistence.find(ACTIVATION), userClient);
        assertThat("tracking is created", tracking, is(notNullValue()));
        assertThat("signature on tracking matches expected",
                tracking.getSignature(),
                is(encoder.encodePassword(tracking.getId() + SERVER_SIDE_SECRET,
                        tracking.getCreatedDate().getMillis())));
    }

    /**
     * Failure case for email tracking log()
     */
    @Test(expected = ConstraintViolationException.class)
    public void badLogging() {
        emailTemplatePersistence.log(null, (UserClient) null);
    }

    /**
     * Load a bad type
     */
    @Test
    public void bad() {
        assertThat("null type should not find anything", emailTemplatePersistence.find(null),
                is(nullValue()));
    }

    /**
     * Make sure we can load email tracking by signature
     */
    @Test
    public void load() {
        UserClient userClient = new UserClient();
        userClient.setEmail("anEmail@matt.com");
        EmailTemplateTracking tracking =
                emailTemplatePersistence.log(emailTemplatePersistence.find(PASSWORD_RESET_NOT_ENABLED),
                        userClient);
        assertThat("can load by signature", emailTemplatePersistence.load(tracking.getSignature()), is(tracking));

        assertThat("load by null is null", emailTemplatePersistence.load(null), is(nullValue()));
    }

    /**
     * Make sure we can use emailAlreadySentToday
     */
    @Test
    public void patientEmailTracking() {
        Patient patient = makeNewRandomPatient(null);
        assertThat("we can query for already sent emails",
                emailTemplatePersistence.emailAlreadySentToday(SCHEDULED_PROGRAM_PATIENT_REMINDER, patient),
                is(false));

        assertThat("null type returns false, not error",
                emailTemplatePersistence.emailAlreadySentToday(null, patient),
                is(false));

        assertThat("null patient returns false, not error",
                emailTemplatePersistence.emailAlreadySentToday(SCHEDULED_PROGRAM_PATIENT_REMINDER, null),
                is(false));

        assertThat("tracking by patient works", emailTemplatePersistence.log(
                emailTemplatePersistence.find(SCHEDULED_PROGRAM_PATIENT_REMINDER), patient), is(notNullValue()));
    }

    /**
     * Try to log a patient email without all of the requisite parts
     */
    @Test(expected = ConstraintViolationException.class)
    public void badPatientTracking() {
        emailTemplatePersistence.log(null, (Patient) null);
    }
}
