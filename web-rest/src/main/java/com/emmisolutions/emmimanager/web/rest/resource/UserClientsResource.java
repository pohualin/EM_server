package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientConflictResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientPage;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientResource;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static com.emmisolutions.emmimanager.model.UserClientSearchFilter.StatusFilter.fromStringOrActive;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Users Client REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class UserClientsResource {

    @Resource
    ClientService clientService;

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientResourceAssembler userClientResourceAssembler;

    @Resource
    UserClientConflictResourceAssembler userClientConflictResourceAssembler;

    /**
     * Get a page of UserClient that satisfy the search criteria
     *
     * @param clientId  to use
     * @param pageable  to use
     * @param sort      to use
     * @param assembler to use
     * @param status    to filter
     * @param term      to search
     * @param teamId    the team to filter by
     * @return UserClientPage
     */
    @RequestMapping(value = "/clients/{clientId}/users", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER"})
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
            @SortDefault(sort = "lastName") Sort sort,
            PagedResourcesAssembler<UserClient> assembler,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "term", required = false) String term,
            @RequestParam(value = "teamId", required = false) Long teamId,
            @RequestParam(value = "tagId", required = false) Long tagId) {

        UserClientSearchFilter filter = new UserClientSearchFilter(new Client(clientId),
                fromStringOrActive(status), term);
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
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER"})
    public ResponseEntity<UserClientResource> createUser(
            @PathVariable Long clientId, @RequestBody UserClient userClient) {

        // look for conflicts before attempting to save
        UserClientResource conflictingUserClient = userClientConflictResourceAssembler
                .toResource(userClientService.findConflictingUsers(userClient));

        if (conflictingUserClient == null) {
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

    /**
     * GET a single UserClient
     *
     * @param id to load
     * @return UserClientResource or NO_CONTENT
     */
    @RequestMapping(value = "/user_client/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER"})
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
    @RequestMapping(value = "/user_client/{id}", method = RequestMethod.PUT, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER"})
    public ResponseEntity<UserClientResource> update(
            @PathVariable("id") Long id, @RequestBody UserClient userClient) {

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