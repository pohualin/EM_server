package com.emmisolutions.emmimanager.web.rest.client.resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import java.util.List;

import javax.annotation.Resource;

import com.emmisolutions.emmimanager.web.rest.client.model.ValidationToken;
import org.springframework.beans.factory.annotation.Value;
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

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserClientValidationEmailService;
import com.emmisolutions.emmimanager.service.UserClientService.UserClientConflict;
import com.emmisolutions.emmimanager.service.mail.MailService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.UserClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.user.ClientUserClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.user.ClientUserConflictResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.client.model.user.UserClientResource;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.security.PermitAll;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;


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
    ResourceAssembler<UserClient, UserClientResource> userResourceAssembler;
    
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

    @Resource
    UserClientValidationEmailService userClientValidationEmailService;

    @Value("${client.application.entry.point:/client.html}")
    String clientEntryPoint;

    private static final String VALIDATION_CLIENT_APPLICATION_URI = "#/validateEmail/%s";
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
     * send validation email
     * @param userId user to get for email personalization
     * @return OK if everything worked
     */
    @RequestMapping(value = "/{userId}/sendEmail", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(@user, #userId)")
    public ResponseEntity<Void> sendValidationEmail(@PathVariable Long userId) {
        // update the validation token, invalidating others
        UserClient savedUserClient = userClientValidationEmailService.addValidationTokenTo(new UserClient(userId));
        if (savedUserClient != null) {
            // get the proper url (the way we make hateoas links), then replace the path with the client entry point
            String validationHref =
                    UriComponentsBuilder.fromHttpUrl(
                            linkTo(methodOn(UserClientsResource.class)
                                    .validateEmail(null)).withSelfRel().getHref())
                                    .replacePath(clientEntryPoint + String.format(VALIDATION_CLIENT_APPLICATION_URI, savedUserClient.getValidationToken()))
                                    .build(false)
                                    .toUriString();
            // send the email (asynchronously)
            mailService.sendValidationEmail(savedUserClient, validationHref);
            userClientService.update(savedUserClient);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.GONE);
    }

    /**
     * validate email token
     * @param validationToken token to validate
     * @return OK if everything worked
     */
    @RequestMapping(value = "/validate/", method = RequestMethod.PUT)
    @PermitAll
    public ResponseEntity<Void> validateEmail(@RequestBody ValidationToken validationToken) {
        UserClient savedUserClient = userClientValidationEmailService.validate(validationToken.getValidationToken());
        if(savedUserClient!=null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    /**
     * GET to retrieve authenticated user.
     *
     * @return UserClientResource when authorized or 401 if the user is not
     *         authorized.
     */
    @RequestMapping(value = "/authenticated", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@startsWith, 'PERM_CLIENT')")
    public ResponseEntity<UserClientResource> authenticated() {
        return new ResponseEntity<>(
                userResourceAssembler.toResource((UserClient) userDetailsService
                        .getLoggedInUser()), HttpStatus.OK);
    }

    /**
     * PUT for updates to a given user client
     * 
     * @param userClientId
     * @param userClient
     * @return
     */
    @RequestMapping(value = "/getById/{userClientId}", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(@user, #userClientId)")
    public ResponseEntity<UserClientResource> updateUserClient(@PathVariable("userClientId")Long userClientId, @RequestBody UserClient userClient) {

        userClient.setId(userClientId);
        
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

    /**
     * GET for a given user client
     * 
     * @param userClientId
     * @return
     */
    @RequestMapping(value = "/getById/{userClientId}", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(@user, #userClientId)")
    public ResponseEntity<UserClientResource> getById(@PathVariable("userClientId") Long userClientId) {
        return new ResponseEntity<>(clientUserClientResourceAssembler.toResource(userDetailsService.get(new UserClient(userClientId))), HttpStatus.OK);
    }
}
