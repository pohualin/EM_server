package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ChangePasswordRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService.UserClientPasswordValidationError;
import com.emmisolutions.emmimanager.service.mail.MailService;
import com.emmisolutions.emmimanager.web.rest.admin.resource.UserClientsResource;
import com.emmisolutions.emmimanager.web.rest.admin.security.RootTokenBasedRememberMeServices;
import com.emmisolutions.emmimanager.web.rest.client.model.password.ForgotPassword;
import com.emmisolutions.emmimanager.web.rest.client.model.password.UserClientPasswordValidationErrorResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Password management resource
 */
@RestController
@RequestMapping(value = "/webapi-client",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class UserClientsPasswordResource {

    @Resource
    UserClientPasswordService userClientPasswordService;

    @Resource
    UserClientPasswordValidationService userClientPasswordValidationService;

    @Resource(name = "userClientPasswordValidationErrorResourceAssembler")
    ResourceAssembler<UserClientPasswordValidationError, UserClientPasswordValidationErrorResource>
            userClientPasswordValidationErrorResourceAssembler;

    @Value("${client.application.entry.point:/client.html}")
    String clientEntryPoint;

    @Resource
    MailService mailService;

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;

    @Resource(name = "clientCsrfAuthenticationStrategy")
    CsrfAuthenticationStrategy clientCsrfAuthenticationStrategy;

    @Resource(name = "clientTokenBasedRememberMeServices")
    RootTokenBasedRememberMeServices tokenBasedRememberMeServices;

    /**
     * Updates the password to the password on the user
     *
     * @param expiredPasswordChangeRequest to change the password
     * @return OK if everything updates successfully
     */
    @RequestMapping(value = "/password/expired", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @PermitAll
    public ResponseEntity<List<UserClientPasswordValidationErrorResource>> changeExpiredPassword(
            @RequestBody ExpiredPasswordChangeRequest expiredPasswordChangeRequest) {

        List<UserClientPasswordValidationError> errors = userClientPasswordValidationService
                .validateRequest(expiredPasswordChangeRequest);
        if (errors.size() == 0) {
            UserClient modifiedUser = userClientPasswordService
                    .changeExpiredPassword(expiredPasswordChangeRequest);
            if (modifiedUser != null) {
                mailService.sendPasswordChangeConfirmationEmail(modifiedUser);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.GONE);
            }
        } else {
            List<UserClientPasswordValidationErrorResource> errorResources = new ArrayList<>();
            for (UserClientPasswordValidationError error : errors) {
                errorResources
                        .add(userClientPasswordValidationErrorResourceAssembler
                                .toResource(error));
            }
            return new ResponseEntity<>(errorResources,
                    HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * PUT to reset a user's password. This method is secured by the user
     * having to know the security questions.
     *
     * @param resetPasswordRequest the activation request
     * @return OK (200) - password reset successful;
     * GONE (410) - reset was invalid;
     * NOT_ACCEPTABLE (406) - problem with password;
     * NOT_AUTHORIZED (403) - user didn't enter the security question answers correctly
     * @see com.emmisolutions.emmimanager.web.rest.client.configuration.security.UserSecurityResponseForResetPasswordRequest#isSecurityResponseValid(ResetPasswordRequest)
     */
    @RequestMapping(value = "/password/reset", method = RequestMethod.PUT, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(@resetPasswordSecurityResponse, #resetPasswordRequest)")
    public ResponseEntity<List<UserClientPasswordValidationErrorResource>> resetPassword(
            @RequestBody ResetPasswordRequest resetPasswordRequest) {

        List<UserClientPasswordValidationError> errors = userClientPasswordValidationService
                .validateRequest(resetPasswordRequest);
        if (errors.size() == 0) {
            UserClient userClient = userClientPasswordService
                    .resetPassword(resetPasswordRequest);
            if (userClient != null) {
                mailService.sendPasswordChangeConfirmationEmail(userClient);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.GONE);
        } else {
            List<UserClientPasswordValidationErrorResource> errorResources = new ArrayList<>();
            for (UserClientPasswordValidationError error : errors) {
                errorResources
                        .add(userClientPasswordValidationErrorResourceAssembler
                                .toResource(error));
            }
            return new ResponseEntity<>(errorResources,
                    HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Update password for existing UserClient
     *
     * @param changePasswordRequest to save new password
     * @return OK if everything went through NOT_ACCEPTABLE if either old
     * password does not match or new password pattern does not meet
     */
    @RequestMapping(value = "/password/change", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(@password, #changePasswordRequest.existingPassword)")
    public ResponseEntity<List<UserClientPasswordValidationErrorResource>> changePassword(
            HttpServletRequest request, HttpServletResponse response,
            @RequestBody ChangePasswordRequest changePasswordRequest) {

        List<UserClientPasswordValidationError> errors = userClientPasswordValidationService
                .validateRequest(changePasswordRequest);
        if (errors.size() == 0) {
            UserClient updated = userClientPasswordService
                    .changePassword(changePasswordRequest);

            mailService.sendPasswordChangeConfirmationEmail(updated);

            // update user's login token
            tokenBasedRememberMeServices.rewriteLoginToken(request, response,
                    updated);

            // update the user's CSRF token
            clientCsrfAuthenticationStrategy.onAuthentication(null, request, response);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            List<UserClientPasswordValidationErrorResource> errorResources = new ArrayList<>();
            for (UserClientPasswordValidationError error : errors) {
                errorResources
                        .add(userClientPasswordValidationErrorResourceAssembler
                                .toResource(error));
            }
            return new ResponseEntity<>(errorResources,
                    HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * GET to find password policy using a reset token
     *
     * @param resetToken to lookup the password policy
     * @return OK or GONE
     */
    @RequestMapping(value = "/password/policy/reset", method = RequestMethod.GET)
    @PermitAll
    public ResponseEntity<ClientPasswordConfiguration> resetPasswordPolicy(
            @RequestParam(value = "token", required = false) String resetToken) {
        ClientPasswordConfiguration clientPasswordConfiguration =
                userClientPasswordService.findPasswordPolicyUsingResetToken(resetToken);
        if (clientPasswordConfiguration != null) {
            return new ResponseEntity<>(clientPasswordConfiguration, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    /**
     * GET to find password policy using an activation token
     *
     * @param activationToken to lookup the password policy
     * @return OK (200): containing ClientPasswordConfiguration
     * <p/>
     * GONE (204): when there isn't one
     */
    @RequestMapping(value = "/password/policy/activation", method = RequestMethod.GET)
    @PermitAll
    public ResponseEntity<ClientPasswordConfiguration> activatePasswordPolicy(
            @RequestParam(value = "token", required = false) String activationToken) {
        ClientPasswordConfiguration clientPasswordConfiguration =
                userClientPasswordService.findPasswordPolicyUsingActivationToken(activationToken);
        if (clientPasswordConfiguration != null) {
            return new ResponseEntity<>(clientPasswordConfiguration, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    /**
     * Load the password policy for an expired user client by id
     *
     * @param clientId to load the policy
     * @return OK (200): containing the ClientPasswordConfiguration
     * <p/>
     * NO_CONTENT (204): when there isn't a configuration (shouldn't happen)
     */
    @RequestMapping(value = "/password/policy/expired/{clientId}", method = RequestMethod.GET)
    @PermitAll
    public ResponseEntity<ClientPasswordConfiguration> passwordPolicy(
            @PathVariable("clientId") Long clientId) {
        ClientPasswordConfiguration clientPasswordConfiguration =
                clientPasswordConfigurationService.get(new Client(clientId));
        if (clientPasswordConfiguration != null) {
            return new ResponseEntity<>(clientPasswordConfiguration, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * PUT to create a forgot password email
     *
     * @param forgotPassword the forget request
     * @return OK (200): no matter what
     */
    @RequestMapping(value = "/password/forgot", method = RequestMethod.PUT)
    @PermitAll
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPassword forgotPassword) {

        UserClient userClient = userClientPasswordService.forgotPassword(forgotPassword.getEmail());
        if (userClient == null) {
            userClient = new UserClient();
            userClient.setEmailValidated(false);
            userClient.setEmail(forgotPassword.getEmail());
        }
        if (userClient.isActive()) {
            // user exists and is active
            if (userClient.isEmailValidated() && userClient.isSecretQuestionCreated() &&
                    StringUtils.isNotBlank(userClient.getPasswordResetToken())) {
                // user has a token created, a validated email and has answered security questions
                String resetRef =
                        UriComponentsBuilder.fromHttpUrl(
                                linkTo(methodOn(UserClientsPasswordResource.class)
                                        .forgotPassword(null)).withSelfRel().getHref())
                                .replacePath(clientEntryPoint +
                                        String.format(UserClientsResource.RESET_PASSWORD_CLIENT_APPLICATION_URI,
                                                userClient.getPasswordResetToken()))
                                .build(false)
                                .toUriString();
                mailService.sendPasswordResetEmail(userClient, resetRef);
            } else {
                // user was found but is not setup for password reset
                mailService.sendPasswordResetNotEnabled(userClient);
            }
        } else {
            // send invalid account reset email
            mailService.sendInvalidAccountPasswordResetEmail(userClient);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
