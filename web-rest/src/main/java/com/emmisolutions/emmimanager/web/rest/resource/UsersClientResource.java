package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientResource;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Users Client REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE,
    APPLICATION_XML_VALUE})
public class UsersClientResource {

    @Resource
    ClientService clientService;

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientResourceAssembler userClientResourceAssembler;

    /**
     * GET to search for clients
     *
     * @param id        the client id
     * @param status    to filter by
     * @param lastName  to filter by
     * @param pageable  paged request
     * @param sort      sorting request
     * @param assembler used to create the PagedResources
     * @return ResponseEntity<UserClientPage> or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/users", method = RequestMethod.GET, consumes = {
        APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_USER_CREATE"})
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "lastName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")})
    public ResponseEntity<UserClientResource> getUsers(
        @PathVariable(value = "clientId") Long id,
        @RequestParam(value = "status", required = false) String status,
        @RequestParam(value = "name", required = false) String lastName,
        @PageableDefault(size = 10, sort = "lastName", direction = Direction.ASC) Pageable pageable,
        @SortDefault(sort = "name") Sort sort,
        PagedResourcesAssembler<UserClient> assembler) {
        return null;
    }

    /**
     * POST to create a new UserClient
     *
     * @param userClient to create
     * @return UserClientResource or INTERNAL_SERVER_ERROR if it could not be
     * created
     * <p/>
     * TODO User create permission
     */
    @RequestMapping(value = "/clients/{clientId}/users", method = RequestMethod.POST, consumes = {
        APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_USER_CREATE"})
    public ResponseEntity<UserClientResource> createUser(
        @RequestBody UserClient userClient) {
        userClient = userClientService.create(userClient);
        if (userClient == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(
                userClientResourceAssembler.toResource(userClient),
                HttpStatus.CREATED);
        }
    }

}
