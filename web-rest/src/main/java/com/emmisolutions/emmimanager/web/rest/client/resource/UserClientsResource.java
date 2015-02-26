package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.mail.MailService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.client.model.user.UserClientResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;


/**
 * User REST API
 */
@RestController("clientUserClientsResource")
@RequestMapping(value = "/webapi-client",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class UserClientsResource {

    @Resource(name = "clientUserDetailsService")
    UserDetailsService userDetailsService;

    @Resource(name = "userClientAuthenticationResourceAssembler")
    ResourceAssembler<User, UserClientResource> userResourceAssembler;

    @Resource
    MailService mailService;

    /**
     * This is an example method that demonstrates how to set up authorization
     * for client and team specific permissions.
     *
     * @param clientId the Client id
     * @param teamId   the Team id
     * @return AUTHORIZED if the logged in user is authorized
     */
    @RequestMapping(value = "/auth-test/{clientId}/{teamId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('PERM_GOD', 'PERM_ADMIN_SUPER_USER', 'PERM_ADMIN_USER') or " +
            "hasPermission(@client.id(#clientId), 'PERM_CLIENT_USER') or " +
            "hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or " +
            "hasPermission(@team.id(#teamId), 'PERM_CLIENT_TEAM_MODIFY_USER_METADATA')"
    )
    public ResponseEntity<String> authorized(@PathVariable Long clientId, @PathVariable Long teamId) {
        return new ResponseEntity<>("AUTHORIZED for client: " + clientId + ", team: " + teamId, HttpStatus.OK);
    }

    /**
     * GET to retrieve authenticated user.
     *
     * @return UserClientResource when authorized or 401 if the user is not authorized.
     */
    @RequestMapping(value = "/authenticated", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('PERM_GOD', 'PERM_ADMIN_SUPER_USER', 'PERM_ADMIN_USER') or hasPermission(@startsWith, 'PERM_CLIENT')")
    public ResponseEntity<UserClientResource> authenticated() {
        return new ResponseEntity<>(userResourceAssembler.toResource(userDetailsService.getLoggedInUser()),
                HttpStatus.OK);
    }

    /**
     * Send an validation email
     * @return OK
     */
    @RequestMapping(value = "/{userId}/validateEmail", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(@user.id(#userId),null)"
    )
    public ResponseEntity<Void> validate(@PathVariable Long userId,
                                         @RequestBody UserClient userClient) {
        if (userClient != null) {
            // send the email (asynchronously)
            mailService.sendValidationEmail(userClient, "http://aUrl");
            userClient.setEmailValidated(true);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
