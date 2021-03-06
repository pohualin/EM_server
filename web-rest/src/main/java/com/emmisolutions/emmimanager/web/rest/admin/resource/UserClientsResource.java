package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.EmailRestrictConfigurationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.mail.MailService;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.*;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsActivationResource;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static com.emmisolutions.emmimanager.model.UserClientSearchFilter.StatusFilter.fromStringOrActive;
import static com.emmisolutions.emmimanager.service.mail.TrackingService.SIGNATURE_VARIABLE_NAME;
import static com.emmisolutions.emmimanager.web.rest.client.resource.TrackingEmailsResource.emailViewedTrackingLink;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Users Client REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class UserClientsResource {

    public static final String RESET_PASSWORD_CLIENT_APPLICATION_URI = "#/reset_password/%s";
    private static final String ACTIVATION_CLIENT_APPLICATION_URI = "#/activate/%s";
    @Resource
    ClientService clientService;
    @Resource
    UserClientService userClientService;
    @Resource
    UserClientPasswordService userClientPasswordService;
    @Resource
    EmailRestrictConfigurationService emailRestrictConfigurationService;
    @Resource
    UserClientResourceAssembler userClientResourceAssembler;
    @Resource
    UserClientConflictResourceAssembler userClientConflictResourceAssembler;
    @Resource
    UserClientValidationErrorResourceAssembler userClientValidationErrorResourceAssembler;
    @Resource
    MailService mailService;

    @Value("${client.application.entry.point:/client.html}")
    String clientEntryPoint;

    /**
     * Get a page of UserClient that satisfy the search criteria
     *
     * @param clientId  to use
     * @param pageable  to use
     * @param assembler to use
     * @param status    to filter
     * @param term      to search
     * @param teamId    the team to filter by
     * @return UserClientPage
     */
    @RequestMapping(value = "/clients/{clientId}/users", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "lastName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", defaultValue = "0", value = "user status filter", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "term", defaultValue = "0", value = "user name filter", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "teamId", defaultValue = "0", value = "team id filter", dataType = "long", paramType = "query")
    })
    public ResponseEntity<UserClientPage> getUsers(
            @PathVariable(value = "clientId") Long clientId,
            @PageableDefault(size = 10, sort = "lastName", direction = Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<UserClient> assembler,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "term", required = false) String term,
            @RequestParam(value = "teamId", required = false) Long teamId,
            @RequestParam(value = "tagId", required = false) Long tagId) {

        UserClientSearchFilter filter = new UserClientSearchFilter(new Client(
                clientId),
                UserClientSearchFilter.StatusFilter
                        .fromStringOrActive(status), term);
        filter.setTeam(new Team(teamId));
        filter.setTag(new Tag(tagId));

        Page<UserClient> userClients = userClientService.list(pageable, filter);

        if (userClients.hasContent()) {
            // create a ClientPage containing the response
            return new ResponseEntity<>(new UserClientPage(
                    assembler.toResource(userClients,
                            userClientResourceAssembler), userClients, filter),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Create a brand new UserClient
     *
     * @param clientId   on which to create
     * @param userClient to create
     * @return UserClient created
     */
    @RequestMapping(value = "/clients/{clientId}/users", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<UserClientResource> createUser(
            @PathVariable Long clientId, @RequestBody UserClient userClient) {

        if (StringUtils.isNotBlank(userClient.getEmail())
                && !userClientService.validateEmailAddress(userClient)) {
            UserClientService.UserClientValidationError validationError = new UserClientService.UserClientValidationError(
                    UserClientService.Reason.EMAIL_RESTRICTION, userClient);
            return new ResponseEntity<>(
                    userClientValidationErrorResourceAssembler
                            .toResource(validationError),
                    HttpStatus.NOT_ACCEPTABLE);
        } else {
            // look for conflicts before attempting to save
            UserClientResource conflictingUserClient = userClientConflictResourceAssembler
                    .toResource(userClientService.findConflictingUsers(userClient));

            if (conflictingUserClient == null) {
                setReloadedClient(clientId, userClient);
                UserClient savedUserClient = userClientService.create(userClient);
                if (savedUserClient != null) {

                    // created a user client successfully
                    return new ResponseEntity<>(
                            userClientResourceAssembler.toResource(userClient),
                            HttpStatus.CREATED);

                } else {
                    // error creating user client
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                // found some conflicting users
                return new ResponseEntity<>(conflictingUserClient,
                        HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }


    /**
     * Send an activation email
     *
     * @param id of the user
     * @return OK
     */
    @RequestMapping(value = "/user_clients/{id}/activate", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<Void> activate(@PathVariable Long id) {

        // update the activation token, invalidating others
        UserClient savedUserClient = userClientService.addActivationKey(new UserClient(id));

        if (savedUserClient != null) {
            // get the proper url (the way we make hateoas links), then replace the path with the client entry point
            String activationHref =
                    UriComponentsBuilder.fromHttpUrl(
                            linkTo(methodOn(UserClientsActivationResource.class)
                                    .activate(null)).withSelfRel().getHref())
                            .replacePath(clientEntryPoint +
                                    String.format(ACTIVATION_CLIENT_APPLICATION_URI,
                                            savedUserClient.getActivationKey())) // activation token
                            .pathSegment(SIGNATURE_VARIABLE_NAME) // tracking token
                            .build(false)
                            .toUriString();

            // send the email (asynchronously)
            mailService.sendActivationEmail(savedUserClient, activationHref, emailViewedTrackingLink());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Expire an activation email
     *
     * @param id of the user
     * @return OK
     */
    @RequestMapping(value = "/user_clients/{id}/activate", method = RequestMethod.DELETE)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<Void> expireActivation(@PathVariable Long id) {
        userClientService.expireActivationToken(new UserClient(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Creates a new reset password token and sends it to the user if the user is found
     *
     * @param id of the user
     * @return OK
     */
    @RequestMapping(value = "/user_clients/{id}/resetPassword", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<Void> resetPassword(@PathVariable Long id) {

        // update the reset token, invalidating others
        UserClient savedUserClient = userClientPasswordService.addResetTokenTo(new UserClient(id));

        // get the proper url (the way we make hateoas links), then replace the path with the client entry point
        String resetRef =
                UriComponentsBuilder.fromHttpUrl(
                        linkTo(methodOn(UserClientsResource.class)
                                .resetPassword(id)).withSelfRel().getHref())
                        .replacePath(clientEntryPoint +
                                String.format(RESET_PASSWORD_CLIENT_APPLICATION_URI,
                                        savedUserClient.getPasswordResetToken())) // reset token
                        .pathSegment(SIGNATURE_VARIABLE_NAME) // tracking token
                        .build(false)
                        .toUriString();

        // send the reset email (asynchronously)
        mailService.sendPasswordResetEmail(savedUserClient, resetRef, emailViewedTrackingLink());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Expire a reset password (either by
     *
     * @param id of the user
     * @return OK
     */
    @RequestMapping(value = "/user_clients/{id}/resetPassword", method = RequestMethod.DELETE)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<Void> expireReset(@PathVariable Long id) {
        userClientPasswordService.expireResetToken(new UserClient(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET a single UserClient
     *
     * @param id to load
     * @return UserClientResource or NO_CONTENT
     */
    @RequestMapping(value = "/user_clients/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<UserClientResource> get(@PathVariable("id") Long id) {
        UserClient userClient = userClientService.reload(new UserClient(id));
        if (userClient != null) {
            return new ResponseEntity<>(
                    userClientResourceAssembler.toResource(userClient),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

    /**
     * PUT to update a single UserClient
     *
     * @param id         to update
     * @param userClient the object to update
     * @return UserClientResource or INTERNAL_SERVER_ERROR if the update somehow
     * returns null
     */
    @RequestMapping(value = "/user_clients/{id}", method = RequestMethod.PUT, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<UserClientResource> update(
            @PathVariable("id") Long id, @RequestBody UserClient userClient) {

        if (StringUtils.isNotBlank(userClient.getEmail())
                && !userClientService.validateEmailAddress(userClient)) {
            UserClientService.UserClientValidationError validationError = new UserClientService.UserClientValidationError(
                    UserClientService.Reason.EMAIL_RESTRICTION, userClient);
            return new ResponseEntity<>(
                    userClientValidationErrorResourceAssembler
                            .toResource(validationError),
                    HttpStatus.NOT_ACCEPTABLE);
        } else {
            // look for conflicts before attempting to save
            UserClientResource conflictingUserClient = userClientConflictResourceAssembler
                    .toResource(userClientService.findConflictingUsers(userClient));

            if (conflictingUserClient == null) {
                UserClient updatedUserClient = userClientService.update(userClient);
                if (updatedUserClient != null) {
                    return new ResponseEntity<>(
                            userClientResourceAssembler
                                    .toResource(updatedUserClient),
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                // found some conflicting users
                return new ResponseEntity<>(conflictingUserClient,
                        HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    /**
     * GET emails that do not follow restrictions
     *
     * @param id        to get from
     * @param pageable  to use
     * @param assembler to use
     * @return UserClientResource or INTERNAL_SERVER_ERROR if the update somehow
     * returns null
     */
    @RequestMapping(value = "/user_clients_bad_emails/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "status", defaultValue = "0", value = "user status filter", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "lastName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query"),
    })
    public ResponseEntity<UserClientPage> badEmails(
            @PathVariable("id") Long id,
            @PageableDefault(size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
            @RequestParam(value = "status", required = false) String status,
            PagedResourcesAssembler<UserClient> assembler) {

        UserClientSearchFilter userClientSearchFilter = new UserClientSearchFilter(new Client(id), fromStringOrActive(status), null);
        Page<UserClient> userClientPage = userClientService.emailsThatDontFollowRestrictions(pageable, userClientSearchFilter);

        if (userClientPage != null && userClientPage.hasContent()) {
            // create a ClientPage containing the response
            return new ResponseEntity<>(new UserClientPage(
                    assembler.toResource(userClientPage, userClientResourceAssembler),
                    userClientPage, userClientSearchFilter), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Get a page of UserClient across all Clients based on the search criteria
     *
     * @param pageable  to use
     * @param assembler to assemble search results
     * @param status    to filter
     * @param term      to search
     * @return a page of UserClient that meet the search criteria
     */
    @RequestMapping(value = "/user_clients", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "client.name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", defaultValue = "0", value = "user status filter", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "term", defaultValue = "0", value = "user name filter", dataType = "string", paramType = "query")
    })
    public ResponseEntity<UserClientPage> list(
            @PageableDefault(size = 10, sort = "normalizedName", direction = Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<UserClient> assembler,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "term", required = false) String term) {

        // Create a filter without client
        UserClientSearchFilter filter = new UserClientSearchFilter(null,
                UserClientSearchFilter.StatusFilter.fromStringOrActive(status),
                term);

        Page<UserClient> userClients = userClientService.list(pageable, filter);

        if (userClients.hasContent()) {
            return new ResponseEntity<>(new UserClientPage(
                    assembler.toResource(userClients,
                            userClientResourceAssembler), userClients, filter),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Get reference data such as status filter for UserClient
     *
     * @return an instance of reference data
     */
    @RequestMapping(value = "/user_clients/ref", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ReferenceData getReferenceData() {
        return new ReferenceData();
    }

    private void setReloadedClient(Long clientId, UserClient userClient) {
        Client client = new Client();
        client.setId(clientId);
        client = clientService.reload(client);
        userClient.setClient(client);
    }

}
