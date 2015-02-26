package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.service.UserClientUserClientRoleService;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.UserClientUserClientRolePage;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.UserClientUserClientRoleResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.UserClientUserClientRoleResourceAssembler;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Users Client REST API
 */
@RestController
@RequestMapping(value = "/webapi", produces = {APPLICATION_JSON_VALUE,
        APPLICATION_XML_VALUE})
public class UserClientUserClientRolesResource {

    @Resource
    UserClientUserClientRoleService userClientUserClientRoleService;

    @Resource
    UserClientUserClientRoleResourceAssembler userClientUserClientRoleResourceAssembler;

    /**
     * Get a Page of UserClientUserClientRole
     *
     * @param userClientId to use
     * @param pageable     to use
     * @param sort         to use
     * @param assembler    to use
     * @return UserClientUserClientRolePage
     */
    @RequestMapping(value = "/user_client/{userClientId}/userClientRoles", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")})
    public ResponseEntity<UserClientUserClientRolePage> getUserClientUserClientRoles(
            @PathVariable(value = "userClientId") Long userClientId,
            @PageableDefault(size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
            @SortDefault(sort = "id") Sort sort,
            PagedResourcesAssembler<UserClientUserClientRole> assembler) {
        Page<UserClientUserClientRole> page = userClientUserClientRoleService
                .findByUserClient(new UserClient(userClientId), pageable);
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
     * Create an UserClientUserClientRolePage
     *
     * @param userClientUserClientRole to be created
     * @param userClientId             the user client's id
     * @return UserClientUserClientRolePage created
     */
    @RequestMapping(value = "/user_client/{userClientId}/userClientRoles", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER"})
    public ResponseEntity<UserClientUserClientRoleResource> associateUserClientRole(
            @PathVariable("userClientId") Long userClientId,
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

    /**
     * @param userClientUserClientRoleId to reload
     * @return NO_CONTENT if no UserClientUserClientRole found by
     * userClientUserClientRoleId or UserClientUserClientRoleResource if
     * UserClientUserClientRole found by userClientUserClientRoleId
     */
    @RequestMapping(value = "/user_client_user_client_role/{userClientUserClientRoleId}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER"})
    public ResponseEntity<UserClientUserClientRoleResource> getUserClientUserClientRole(
            @PathVariable(value = "userClientUserClientRoleId") Long userClientUserClientRoleId) {
        UserClientUserClientRole usucr = userClientUserClientRoleService
                .reload(new UserClientUserClientRole(userClientUserClientRoleId));
        if (usucr != null) {
            return new ResponseEntity<>(
                    userClientUserClientRoleResourceAssembler.toResource(usucr),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * DELETE a single UserClientUserClientRole
     *
     * @param userClientUserClientRoleId to remove
     */
    @RequestMapping(value = "/user_client_user_client_role/{userClientUserClientRoleId}", method = RequestMethod.DELETE)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER"})
    public void delete(@PathVariable Long userClientUserClientRoleId) {
        userClientUserClientRoleService.delete(new UserClientUserClientRole(
                userClientUserClientRoleId));
    }
}
