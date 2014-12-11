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

import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.service.UserClientUserClientTeamRoleService;
import com.emmisolutions.emmimanager.web.rest.model.user.client.team.UserClientUserClientTeamRoleResource;
import com.emmisolutions.emmimanager.web.rest.model.user.client.team.UserClientUserClientTeamRoleResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.user.client.team.UserClientUserClientTeamRoleResourcePage;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;

/**
 * User Client User Client Team Roles REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = { APPLICATION_JSON_VALUE,
	APPLICATION_XML_VALUE })
public class UserClientUserClientTeamRolesResource {

    @Resource
    UserClientUserClientTeamRoleService userClientUserClientTeamRoleService;

    @Resource
    UserClientUserClientTeamRoleResourceAssembler userClientUserClientTeamRoleResourceAssembler;

    @RequestMapping(value = "/user_client/{userClientId}/userClientTeamRoles", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
	    "PERM_CLIENT_CREATE_NEW_USER" })
    @ApiImplicitParams(value = {
	    @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
	    @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
	    @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query") })
    public ResponseEntity<UserClientUserClientTeamRoleResourcePage> getUserClientUserClientTeamRoles(
	    @PathVariable(value = "userClientId") Long userClientId,
	    @PageableDefault(size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
	    @SortDefault(sort = "id") Sort sort,
	    PagedResourcesAssembler<UserClientUserClientTeamRole> assembler) {
	Page<UserClientUserClientTeamRole> page = userClientUserClientTeamRoleService
		.findByUserClient(userClientId, pageable);
	if (page != null) {
	    return new ResponseEntity<>(
		    new UserClientUserClientTeamRoleResourcePage(
			    assembler
				    .toResource(page,
					    userClientUserClientTeamRoleResourceAssembler),
			    page), HttpStatus.OK);
	} else {
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    }

    @RequestMapping(value = "/user_client/{userClientId}/userClientTeamRoles", method = RequestMethod.POST, consumes = {
	    APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
	    "PERM_CLIENT_CREATE_NEW_USER" })
    public ResponseEntity<UserClientUserClientTeamRoleResource> associateUserClientTeamRole(
	    @RequestBody UserClientUserClientTeamRole userClientUserClientTeamRole) {
	userClientUserClientTeamRole = userClientUserClientTeamRoleService
		.create(userClientUserClientTeamRole);
	if (userClientUserClientTeamRole == null) {
	    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	} else {
	    return new ResponseEntity<>(
		    userClientUserClientTeamRoleResourceAssembler
			    .toResource(userClientUserClientTeamRole),
		    HttpStatus.CREATED);
	}
    }

    @RequestMapping(value = "/user_client_user_client_team_role/{userClientUserClientTeamRoleId}", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
	    "PERM_CLIENT_CREATE_NEW_USER" })
    public ResponseEntity<UserClientUserClientTeamRoleResource> getUserClientUserClientTeamRole(
	    @PathVariable(value = "userClientUserClientTeamRoleId") Long userClientUserClientTeamRoleId) {
	UserClientUserClientTeamRole usuctr = userClientUserClientTeamRoleService
		.reload(userClientUserClientTeamRoleId);
	if (usuctr != null) {
	    return new ResponseEntity<>(
		    userClientUserClientTeamRoleResourceAssembler
			    .toResource(usuctr),
		    HttpStatus.OK);
	} else {
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
    }

    @RequestMapping(value = "/user_client_user_client_team_role/{userClientUserClientTeamRoleId}", method = RequestMethod.DELETE)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
	    "PERM_CLIENT_CREATE_NEW_USER" })
    public void delete(@PathVariable Long userClientUserClientTeamRoleId) {
	userClientUserClientTeamRoleService
		.delete(userClientUserClientTeamRoleId);
    }
}
