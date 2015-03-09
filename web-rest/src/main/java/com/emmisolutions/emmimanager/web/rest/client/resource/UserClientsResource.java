package com.emmisolutions.emmimanager.web.rest.client.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserClientService.UserClientConflict;
import com.emmisolutions.emmimanager.service.mail.MailService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.UserClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.user.ClientUserClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.user.ClientUserConflictResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.user.UserClientResource;

/**
 * User REST API
 */
@RestController("clientUserClientsResource")
@RequestMapping(value = "/webapi-client", produces = { APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE })
public class UserClientsResource {

    @Resource(name = "clientUserDetailsService")
    UserDetailsService userDetailsService;

    @Resource(name = "userClientAuthenticationResourceAssembler")
    ResourceAssembler<User, UserClientResource> userResourceAssembler;

    @Resource
    UserClientResourceAssembler userClientResourceAssembler;

    @Resource
    ClientUserConflictResourceAssembler conflictsResourceAssembler;

    @Resource
    ClientUserClientResourceAssembler clientUserClientResourceAssembler;

    @Resource
    MailService mailService;

    @Resource
    UserClientService userClientService;

    /**
     * This is an example method that demonstrates how to set up authorization
     * for client and team specific permissions.
     *
     * @param clientId the Client id
     * @param teamId the Team id
     * @return AUTHORIZED if the logged in user is authorized
     */
    @RequestMapping(value = "/auth-test/{clientId}/{teamId}/{userId}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@client.id(#clientId), 'PERM_CLIENT_SUPER_USER') or "
            + "hasPermission(@team.id(#teamId), 'PERM_CLIENT_TEAM_MODIFY_USER_METADATA') or "
            + "hasPermission(@password, #pw) or "
            + "hasPermission(@user, #userId)")
    public ResponseEntity<String> authorized(@PathVariable Long clientId,
            @PathVariable Long teamId, @PathVariable Long userId,
            @RequestParam(required = false) String pw) {
        return new ResponseEntity<>("AUTHORIZED for client: " + clientId
                + ", team: " + teamId + ", user: " + userId + ", pw: " + pw,
                HttpStatus.OK);
    }

    /**
     * Send an validation email
     * 
     * @return OK
     */
    @RequestMapping(value = "/{userId}/validateEmail", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(@user, #userId)")
    public ResponseEntity<Void> validate(@PathVariable Long userId,
            @RequestBody UserClient userClient) {
        if (userClient != null) {
            // send the email (asynchronously)
            userClient.setId(userId);
            userClient = userClientService.reload(userClient);
            mailService.sendValidationEmail(userClient, "http://aUrl");
            userClient.setEmailValidated(true);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * GET to retrieve authenticated user.
     *
     * @return UserClientResource when authorized or 401 if the user is not
     *         authorized.
     */
    @RequestMapping(value = "/authenticated", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('PERM_GOD', 'PERM_ADMIN_SUPER_USER', 'PERM_ADMIN_USER') or hasPermission(@startsWith, 'PERM_CLIENT')")
    public ResponseEntity<UserClientResource> authenticated() {
        return new ResponseEntity<>(
                userResourceAssembler.toResource((UserClient) userDetailsService
                        .getLoggedInUser()), HttpStatus.OK);
    }

    @RequestMapping(value = "/authenticated", method = RequestMethod.PUT)
    public ResponseEntity<UserClientResource> updateUserClient(@RequestBody UserClient userClient) {

        // look for conflicts before attempting to save
        List<UserClientConflict> conflicts = userClientService.findConflictingUsers(userClient);

        if (conflicts.isEmpty()) {
            UserClient updatedUserClient = userClientService.update(userClient);
            if (updatedUserClient != null) {
                return new ResponseEntity<>(clientUserClientResourceAssembler.toResource(updatedUserClient), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            // found some conflicting users
            return new ResponseEntity<>(conflictsResourceAssembler.toResource(conflicts), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "/getById/{userClientId}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@user, #userClientid)")
    public ResponseEntity<UserClientResource> getById(@PathVariable("userClientId") Long userClientid) {
        return new ResponseEntity<>(clientUserClientResourceAssembler.toResource((UserClient) userDetailsService.get(new UserClient(userClientid))), HttpStatus.OK);
    }
}
