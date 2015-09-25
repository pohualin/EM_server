package com.emmisolutions.emmimanager.service.spring.mail;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.mail.MailService;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static com.emmisolutions.emmimanager.service.mail.TrackingService.SIGNATURE_VARIABLE_NAME;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Test email creation
 */
public class EmailServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    MailService mailService;

    @Resource
    JavaMailSenderImpl javaMailSender;

    @Resource
    UserClientService userClientService;

    /**
     * Ensure that an activation email is sent out to the user
     *
     * @throws IOException        on read error of email
     * @throws MessagingException on read error of email
     */
    @Test
    public void sendActivationEmail() throws IOException, MessagingException {
        // make a new user
        UserClient user = userClientService
                .addActivationKey(makeNewRandomUserClient(null));

        setEmailMailServerUser(user.getEmail());

        // send the activation email
        String activationUrl = "http://whatever/" + user.getActivationKey();
        mailService.sendActivationEmail(user, activationUrl, null);

        // make sure the message was received
        MimeMessage[] messages = getEmailsFromServer();

        int current = messages.length - 1 == -1 ? 0 : messages.length - 1;


        assertThat("user's login should be present",
                messages[current].getContent().toString().indexOf(user.getLogin()), is(not(-1)));

        assertThat("title of email comes from template", messages[current].getHeader("Subject", ":"),
                is("New User Account Confirmation"));

        assertThat("activation url is present",
                messages[current].getContent().toString().indexOf(activationUrl), is(not(-1)));
    }

    /**
     * Try to send activation email in various 'bad' scenarios
     */
    @Test
    public void badActivation() {
        UserClient userClient = new UserClient();

        int countOfMessages = getEmailsFromServer().length;

        mailService.sendActivationEmail(null, null, null);
        assertThat("no client shouldn't change messages", getEmailsFromServer().length, is(countOfMessages));

        mailService.sendActivationEmail(userClient, "http://aUrl", null);
        assertThat("no email shouldn't send", getEmailsFromServer().length, is(countOfMessages));

        userClient.setEmail("steve@steve.com");
        mailService.sendActivationEmail(userClient, "http://aUrl", null);
        assertThat("everything ok, send", getEmailsFromServer().length, is(countOfMessages + 1));
    }


    /**
     * Ensure that a password reset email is sent out to the user
     *
     * @throws IOException        on read error of email
     * @throws MessagingException on read error of email
     */
    @Test
    public void sendPasswordResetEmail() throws IOException, MessagingException {
        // make a new user
        UserClient user = makeNewRandomUserClient(null);

        setEmailMailServerUser(user.getEmail());

        // make sure the message was received
        MimeMessage[] messages = getEmailsFromServer();

        int before = messages.length - 1 == -1 ? 0 : messages.length - 1;

        // send the reset email
        String resetUrl = "http://whatever/";
        mailService.sendPasswordResetEmail(user, resetUrl, null);

        // make sure the message was received
        messages = getEmailsFromServer();

        int current = messages.length - 1 == -1 ? 0 : messages.length - 1;

        assertThat("a mail was sent", before + 1, is(current));

        assertThat("user's first name should be present",
                messages[current].getContent().toString().indexOf(StringUtils.capitalize(user.getFirstName())), is(not(-1)));

        assertThat("title of email comes from template", messages[current].getHeader("Subject", ":"),
                is("Password Reset"));

        assertThat("reset url is present",
                messages[current].getContent().toString().indexOf(resetUrl), is(not(-1)));
    }

    /**
     * Try to send activation email in various 'bad' scenarios
     */
    @Test
    public void badPasswordReset() {
        UserClient userClient = new UserClient();

        int countOfMessages = getEmailsFromServer().length;

        mailService.sendPasswordResetEmail(null, null, null);
        assertThat("no client shouldn't change messages", getEmailsFromServer().length, is(countOfMessages));

        mailService.sendPasswordResetEmail(userClient, "http://aUrl", null);
        assertThat("no email shouldn't send", getEmailsFromServer().length, is(countOfMessages));

        userClient.setEmail("steve@steve.com");
        mailService.sendPasswordResetEmail(userClient, "http://aUrl", null);
        assertThat("everything ok, send", getEmailsFromServer().length, is(countOfMessages + 1));
    }

    /**
     * Ensure we can send invalid account password reset emails
     */
    @Test
    public void invalidAccountPasswordReset() {
        String email = "invalid@account.org";
        setEmailMailServerUser(email);
        int countOfMessages = getEmailsFromServer().length;
        mailService.sendInvalidAccountPasswordResetEmail(null, null);
        assertThat("no email shouldn't send", getEmailsFromServer().length, is(countOfMessages));

        UserClient userClient = new UserClient();
        userClient.setEmail(email);
        mailService.sendInvalidAccountPasswordResetEmail(userClient, null);
        assertThat("everything ok, send", getEmailsFromServer().length, is(countOfMessages + 1));
    }


    /**
     * Ensure we can send password change confirmation emails
     */
    @Test
    public void changePasswordConfirmation() {
        String email = "changed.password@account.org";
        setEmailMailServerUser(email);
        int countOfMessages = getEmailsFromServer().length;
        mailService.sendPasswordChangeConfirmationEmail(null, null);
        assertThat("email shouldn't send", getEmailsFromServer().length, is(countOfMessages));

        UserClient userClient = new UserClient();
        userClient.setEmail(email);
        mailService.sendPasswordChangeConfirmationEmail(userClient, null);
        assertThat("everything ok, send", getEmailsFromServer().length, is(countOfMessages + 1));
    }

    /**
     * Ensure we can send email verification emails
     */
    @Test
    public void emailVerification() {
        String email = "email.verified@account.org";
        setEmailMailServerUser(email);
        int countOfMessages = getEmailsFromServer().length;
        mailService.sendValidationEmail(null, "http://aUrl", "http://aTrackingUrl/" + SIGNATURE_VARIABLE_NAME);
        assertThat("email shouldn't send", getEmailsFromServer().length, is(countOfMessages));

        UserClient userClient = new UserClient();
        userClient.setEmail(email);
        mailService.sendValidationEmail(userClient, "http://aUrl", null);
        assertThat("everything ok, send", getEmailsFromServer().length, is(countOfMessages + 1));
    }
}
