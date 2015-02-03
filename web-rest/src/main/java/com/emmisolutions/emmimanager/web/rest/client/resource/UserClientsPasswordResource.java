package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

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

    /**
     * Updates the password to the password on the user
     *
     * @param expiredPasswordChangeRequest  to change the password
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
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.GONE);
    }
}
