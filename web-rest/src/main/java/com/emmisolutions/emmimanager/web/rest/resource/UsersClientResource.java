package com.emmisolutions.emmimanager.web.rest.resource;

import static com.emmisolutions.emmimanager.model.UserClientSearchFilter.StatusFilter.fromStringOrActive;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientPage;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientResource;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;

/**
 * Users Client REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
	APPLICATION_XML_VALUE })
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
     * @param id
     *            the client id
     * @param status
     *            to filter by
     * @param lastName
     *            to filter by
     * @param pageable
     *            paged request
     * @param sort
     *            sorting request
     * @param assembler
     *            used to create the PagedResources
     * @return ResponseEntity<UserClientPage> or NO_CONTENT
     */
    @RequestMapping(value = "/clients/{clientId}/users", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
	    "PERM_CLIENT_CREATE_NEW_USER" })
    @ApiImplicitParams(value = {
	    @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
	    @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
	    @ApiImplicitParam(name = "sort", defaultValue = "lastName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query") })
    public ResponseEntity<UserClientPage> getUsers(
	    @PathVariable(value = "clientId") Long clientId,
	    @PageableDefault(size = 10, sort = "lastName", direction = Direction.ASC) Pageable pageable,
	    @SortDefault(sort = "lastName") Sort sort,
	    PagedResourcesAssembler<UserClient> assembler,
	    @RequestParam(value = "status", required = false) String status,
	    @RequestParam(value = "term", required = false) String term) {

	UserClientSearchFilter filter = new UserClientSearchFilter(clientId,
		fromStringOrActive(status), term);

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
     * POST to create a new UserClient
     *
     * @param userClient
     *            to create
     * @return UserClientResource or INTERNAL_SERVER_ERROR if it could not be
     *         created
     *         <p/>
     *         TODO User create permission
     */
    @RequestMapping(value = "/clients/{clientId}/users", method = RequestMethod.POST, consumes = {
	    APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
	    "PERM_CLIENT_CREATE_NEW_USER" })
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

    /**
     * GET a single user client
     *
     * @param id
     *            to load
     * @return UserClientResource or NO_CONTENT
     */
    @RequestMapping(value = "/user_client/{id}", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
	    "PERM_CLIENT_CREATE_NEW_USER" })
    public ResponseEntity<UserClientResource> get(@PathVariable("id") Long id) {
	UserClient userClient = userClientService.reload(id);
	if (userClient != null) {
	    return new ResponseEntity<>(
		    userClientResourceAssembler.toResource(userClient),
		    HttpStatus.OK);
	} else {
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

    }

}
