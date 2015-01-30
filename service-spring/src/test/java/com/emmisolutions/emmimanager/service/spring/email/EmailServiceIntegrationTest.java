package com.emmisolutions.emmimanager.service.spring.email;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.mail.MailService;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

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
        mailService.sendActivationEmail(user, activationUrl);

        // make sure the message was received
        MimeMessage[] messages = getEmailsFromServer();

        assertThat("user's login should be present",
                messages[0].getContent().toString().indexOf(user.getLogin()), is(not(-1)));

        assertThat("title of email comes from template", messages[0].getHeader("Subject", ":"),
                is("New User Account Confirmation"));

        assertThat("activation url is present",
                messages[0].getContent().toString().indexOf(activationUrl), is(not(-1)));
    }

    /**
     * Try to send activation email in various 'bad' scenarios
     */
    @Test
    public void bad() {
        UserClient userClient = new UserClient();

        int countOfMessages = getEmailsFromServer().length;

        mailService.sendActivationEmail(null, null);
        assertThat("no client shouldn't change messages", getEmailsFromServer().length, is(countOfMessages));

        mailService.sendActivationEmail(userClient, null);
        assertThat("no client activation key", getEmailsFromServer().length, is(countOfMessages));

        userClient.setActivationKey("whatever");
        mailService.sendActivationEmail(userClient, null);
        assertThat("no base url", getEmailsFromServer().length, is(countOfMessages));

        mailService.sendActivationEmail(userClient, "http://aUrl");
        assertThat("no email shouldn't send", getEmailsFromServer().length, is(countOfMessages));

        userClient.setEmail("steve@steve.com");
        mailService.sendActivationEmail(userClient, "http://aUrl");
        assertThat("everything ok, send", getEmailsFromServer().length, is(countOfMessages + 1));
    }

}
