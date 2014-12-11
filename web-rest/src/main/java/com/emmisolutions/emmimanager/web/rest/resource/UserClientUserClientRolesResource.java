package com.emmisolutions.emmimanager.web.rest.resource;

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
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.service.UserClientUserClientRoleService;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientUserClientRolePage;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientUserClientRoleResource;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientUserClientRoleResourceAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;

/**
 * Users Client REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
	APPLICATION_XML_VALUE })
public class UserClientUserClientRolesResource {

    @Resource
    UserClientUserClientRoleService userClientUserClientRoleService;

    @Resource
    UserClientUserClientRoleResourceAssembler userClientUserClientRoleResourceAssembler;

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
    @RequestMapping(value = "/user_client/{userClientId}/userClientRoles", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
	    "PERM_CLIENT_CREATE_NEW_USER" })
    @ApiImplicitParams(value = {
	    @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
	    @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
	    @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query") })
    public ResponseEntity<UserClientUserClientRolePage> getUserClientUserClientRoles(
	    @PathVariable(value = "userClientId") Long userClientId,
	    @PageableDefault(size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
	    @SortDefault(sort = "id") Sort sort,
	    PagedResourcesAssembler<UserClientUserClientRole> assembler) {
	Page<UserClientUserClientRole> page = userClientUserClientRoleService
		.findByUserClient(userClientId, pageable);
	if (page != null) {
	    return new ResponseEntity<>(new UserClientUserClientRolePage(
		    assembler.toResource(page,
			    userClientUserClientRoleResourceAssembler), page),
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
    @RequestMapping(value = "/user_client/{userClientId}/userClientRoles", method = RequestMethod.POST, consumes = {
	    APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
	    "PERM_CLIENT_CREATE_NEW_USER" })
    public ResponseEntity<UserClientUserClientRoleResource> associateUserClientRole(
	    @RequestBody UserClientUserClientRole userClientUserClientRole) {
	userClientUserClientRole = userClientUserClientRoleService
		.create(userClientUserClientRole);
	if (userClientUserClientRole == null) {
	    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	} else {
	    return new ResponseEntity<>(
		    userClientUserClientRoleResourceAssembler
			    .toResource(userClientUserClientRole),
		    HttpStatus.CREATED);
	}
    }

}
