package com.emmisolutions.emmimanager.service.spring.mail;

import com.emmisolutions.emmimanager.model.mail.EmailTemplateTracking;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateType;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import com.emmisolutions.emmimanager.service.UserClientValidationEmailService;
import com.emmisolutions.emmimanager.service.mail.MailService;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.emmisolutions.emmimanager.model.mail.EmailTemplateType.*;
import static com.emmisolutions.emmimanager.service.mail.TrackingService.SIGNATURE_VARIABLE_NAME;

/**
 * MailService implementation
 */
@Service
public class MailServiceImpl implements MailService {

    public static final String USER_CONTEXT_VARIABLE = "user";
    public static final String URL_CONTEXT_VARIABLE = "activationUrl";
    public static final String EMAIL_VALIDATION_TIMEOUT_CONTEXT_VARIABLE = "emailValidationTimeout";
    public static final String EMAIL_TRACKING_CONTEXT_VARIABLE = "emailTracking";
    public static final String SCHEDULED_PROGRAM_CONTEXT_VARIABLE = "scheduledProgram";

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
    private static final Pattern titleFinderInTemplates = Pattern.compile("<title>(.*)</title>", Pattern.DOTALL);
    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private TemplateEngine templateEngine;

    @Value("${mail.activation.from:EmmiManager Activation <no_reply_act@emmisolutions.com>}")
    private String activationFrom;

    @Value("${mail.validation.from:EmmiManager Email Validation <no_reply_validation@emmisolutions.com>}")
    private String emailValidationFrom;

    @Value("${mail.password_reset.from:EmmiManager Password Reset <no_reply_pw@emmisolutions.com>}")
    private String passwordResetFrom;

    @Value("${mail.password_changed.from:EmmiManager Password Manager <no_reply_pw_manager@emmisolutions.com>}")
    private String passwordChangedFrom;

    @Value("${mail.scheduled_program_from.from:Emmi Solutions <no_reply_program_manager@emmisolutions.com>}")
    private String reminderFrom;

    @Value("${mail.server.use:true}")
    private boolean useMailServer;

    @Resource
    private EmailTemplatePersistence emailTemplatePersistence;

    @Async
    @Override
    @Transactional
    public void sendActivationEmail(UserClient user, String activationUrl, String trackingUrl) {
        EmailTemplateTracking emailSentTracking = emailTemplatePersistence.log(emailTemplatePersistence.find(ACTIVATION), user);
        sendTemplateBasedEmail(activationFrom, user, activationUrl, ACTIVATION, trackingUrl, null, emailSentTracking);
    }

    @Async
    @Override
    @Transactional
    public void sendValidationEmail(UserClient user, String validationUrl, String trackingUrl) {
        EmailTemplateTracking emailSentTracking = emailTemplatePersistence.log(emailTemplatePersistence.find(VALIDATION), user);
        sendTemplateBasedEmail(emailValidationFrom, user, validationUrl, VALIDATION, trackingUrl, null, emailSentTracking);
    }

    @Async
    @Override
    @Transactional
    public void sendPasswordResetEmail(UserClient user, String passwordResetUrl, String trackingUrl) {
        EmailTemplateTracking emailSentTracking = emailTemplatePersistence.log(emailTemplatePersistence.find(PASSWORD_RESET), user);
        sendTemplateBasedEmail(passwordResetFrom, user, passwordResetUrl, PASSWORD_RESET,
                trackingUrl, null, emailSentTracking);
    }

    @Async
    @Override
    @Transactional
    public void sendInvalidAccountPasswordResetEmail(UserClient userClient, String trackingUrl) {
        EmailTemplateTracking emailSentTracking = emailTemplatePersistence.log(emailTemplatePersistence.find(PASSWORD_RESET_INVALID_ACCOUNT), userClient);
        sendTemplateBasedEmail(passwordResetFrom, userClient, null, PASSWORD_RESET_INVALID_ACCOUNT,
                trackingUrl, null, emailSentTracking);
    }

    @Async
    @Override
    @Transactional
    public void sendPasswordChangeConfirmationEmail(UserClient userClient, String trackingUrl) {
        EmailTemplateTracking emailSentTracking = emailTemplatePersistence.log(emailTemplatePersistence.find(PASSWORD_CHANGED), userClient);
        sendTemplateBasedEmail(passwordChangedFrom, userClient, null, PASSWORD_CHANGED, trackingUrl, null, emailSentTracking);
    }

    @Async
    @Override
    @Transactional
    public void sendPasswordResetNotEnabled(UserClient userClient, String trackingUrl) {
        EmailTemplateTracking emailSentTracking = emailTemplatePersistence.log(emailTemplatePersistence.find(PASSWORD_RESET_NOT_ENABLED), userClient);
        sendTemplateBasedEmail(passwordResetFrom, userClient, null, PASSWORD_RESET_NOT_ENABLED, trackingUrl, null, emailSentTracking);
    }

    @Override
    @Transactional
    public void sendPatientScheduledProgramReminderEmail(ScheduledProgram scheduledProgram, String startEmmiUrl, String trackingUrl) {
        UserClient stub = new UserClient();
        stub.setEmail(scheduledProgram.getPatient().getEmail());
        stub.setFirstName(scheduledProgram.getPatient().getFirstName());
        stub.setLastName(scheduledProgram.getPatient().getLastName());
        EmailTemplateTracking emailSentTracking = emailTemplatePersistence.log(emailTemplatePersistence.find(SCHEDULED_PROGRAM_PATIENT_REMINDER),
                scheduledProgram.getPatient());
        sendTemplateBasedEmail(reminderFrom, stub, startEmmiUrl,
                SCHEDULED_PROGRAM_PATIENT_REMINDER, trackingUrl, scheduledProgram, emailSentTracking);
    }

    private void sendTemplateBasedEmail(String from, UserClient user, String url, EmailTemplateType type,
                                        String trackingUrl, ScheduledProgram scheduledProgram,
                                        EmailTemplateTracking emailSentTracking) {
        if (user == null || StringUtils.isBlank(user.getEmail()) || type == null) {
            return;
        }
        Context context = new Context();
        context.setVariable(USER_CONTEXT_VARIABLE, user);
        context.setVariable(URL_CONTEXT_VARIABLE, url);
        context.setVariable(EMAIL_VALIDATION_TIMEOUT_CONTEXT_VARIABLE,
                UserClientValidationEmailService.VALIDATION_TOKEN_HOURS_VALID);
        if (StringUtils.isNotBlank(trackingUrl) && emailSentTracking != null) {
            context.setVariable(EMAIL_TRACKING_CONTEXT_VARIABLE,
                    trackingUrl.replace(SIGNATURE_VARIABLE_NAME, emailSentTracking.getSignature()));
        }
        if (scheduledProgram != null) {
            context.setVariable(SCHEDULED_PROGRAM_CONTEXT_VARIABLE, scheduledProgram);
        }

        LOGGER.debug("User Context: {}", context.getVariables().get(USER_CONTEXT_VARIABLE));
        LOGGER.debug("URL Context: {}", context.getVariables().get(URL_CONTEXT_VARIABLE));
        LOGGER.debug("Validation Timeout Context: {}",
                context.getVariables().get(EMAIL_VALIDATION_TIMEOUT_CONTEXT_VARIABLE));
        LOGGER.debug("Email Tracking Context: {}", context.getVariables().get(EMAIL_TRACKING_CONTEXT_VARIABLE));
        LOGGER.debug("Scheduled Program Context: {}",
                context.getVariables().get(SCHEDULED_PROGRAM_CONTEXT_VARIABLE));

        String content = templateEngine.process("db:" + type.toString(), context);
        String subject = "Action Required";

        // override default title
        Matcher matcher = titleFinderInTemplates.matcher(content);
        if (matcher.find()) {
            subject = StringUtils.trimToNull(matcher.group(1));
        }
        String to = String.format("%s <%s>", StringUtils.defaultIfBlank(user.getFullName(), user.getEmail()), user.getEmail());

        // send the email
        sendEmail(to, from, subject, content, false, true, emailSentTracking);
    }

    private void sendEmail(String to, String from, String subject,
                           String content, boolean isMultipart, boolean isHtml,
                           EmailTemplateTracking emailSentTracking) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            LOGGER.debug("=======================EMAIL START=========================");
            LOGGER.debug("|To: {}", to);
            LOGGER.debug("|From: {}", from);
            LOGGER.debug("|Subject: {}", subject);
            LOGGER.debug("|Body: {}", content.replaceAll("[\\t\\n\\r]", ""));
            LOGGER.debug("========================EMAIL END==========================");
            LOGGER.debug("Sending to actual mail server: {}...", useMailServer);
            if (useMailServer) {
                javaMailSender.send(mimeMessage);
            }
            emailSentTracking.setSent(true);
        } catch (Exception e) {
            LOGGER.warn("E-mail could not be sent to user '{}'", to, e);
        }
    }

}
