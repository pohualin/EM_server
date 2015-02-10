package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.mail.MailService;
import com.emmisolutions.emmimanager.web.rest.admin.resource.UserClientsResource;
import com.emmisolutions.emmimanager.web.rest.client.model.password.ForgotPassword;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

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

    @Value("${client.application.entry.point:/client.html}")
    String clientEntryPoint;

    @Resource
    MailService mailService;

    /**
     * Updates the password to the password on the user
     *
     * @param expiredPasswordChangeRequest to change the password
     * @return OK if everything updates successfully
     */
    @RequestMapping(value = "/password/expired", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @PermitAll
    public ResponseEntity<Void> changeExpiredPassword(@RequestBody ExpiredPasswordChangeRequest expiredPasswordChangeRequest) {
        userClientPasswordService.changeExpiredPassword(expiredPasswordChangeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * PUT to reset a user's password
     *
     * @param resetPasswordRequest the activation request
     * @return OK or GONE
     */
    @RequestMapping(value = "/password/reset", method = RequestMethod.PUT)
    @PermitAll
    public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        UserClient userClient = userClientPasswordService.resetPassword(resetPasswordRequest);
        if (userClient != null) {
            mailService.sendPasswordChangeConfirmationEmail(userClient);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    /**
     * PUT to create a forgot password email
     *
     * @param email the forget request
     * @return OK
     */
    @RequestMapping(value = "/password/forgot", method = RequestMethod.PUT)
    @PermitAll
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPassword forgotPassword) {

        UserClient userClient = userClientPasswordService.forgotPassword(forgotPassword.getEmail());
        if (userClient == null){
            userClient = new UserClient();
            userClient.setEmailValidated(false);
            userClient.setEmail(forgotPassword.getEmail());
        }
        if (userClient.isEmailValidated()) {
            // has a validated email
            String resetRef =
                    UriComponentsBuilder.fromHttpUrl(
                            linkTo(methodOn(UserClientsPasswordResource.class)
                                    .forgotPassword(null)).withSelfRel().getHref())
                            .replacePath(clientEntryPoint +
                                    String.format(UserClientsResource.RESET_PASSWORD_CLIENT_APPLICATION_URI,
                                            userClient.getPasswordResetToken()))
                            .build(false)
                            .toUriString();
            // send account reset email
            mailService.sendPasswordResetEmail(userClient, resetRef);
        } else {
            // send invalid account reset email
            mailService.sendInvalidAccountPasswordResetEmail(userClient);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
