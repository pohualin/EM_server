package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.UserClientPassword;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Password management resource
 */
@RestController
@RequestMapping(value = "/webapi",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class UserClientPasswordResource {

    @Resource
    UserClientPasswordService userClientPasswordService;

    /**
     * Updates the password to the password on the user
     *
     * @param userId to update the password for
     * @param password   where the new password is located
     * @return OK if everything updates successfully
     */
    @RequestMapping(value = "/user_client/{userId}/password/set", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    public ResponseEntity<Void> set(@PathVariable Long userId, @RequestBody UserClientPassword password) {
        UserClient toUpdate = new UserClient(userId);
        toUpdate.setPassword(password.getPassword());
        userClientPasswordService.updatePassword(toUpdate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
