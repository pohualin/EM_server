package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.UserClientUserClientTeamRoleSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.service.UserClientUserClientTeamRoleService;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.UserClientUserClientTeamRoleResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.UserClientUserClientTeamRoleResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.UserClientUserClientTeamRoleResourcePage;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

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

    /**
     * Get a page of UserClientUserClientTeamRole with userClientId and
     * userClientTeamRoleId
     * 
     * @param userClientId
     *            to lookup
     * @param pageable
     *            to use
     * @param assembler
     *            to assemble resource
     * @param userClientTeamRoleId
     *            to lookup
     * @return a page of UserClientUserClientTeamRole
     */
    @RequestMapping(value = "/user_client/{userClientId}/userClientUserClientTeamRoles", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER" })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query") })
    public ResponseEntity<UserClientUserClientTeamRoleResourcePage> getUserClientUserClientTeamRoles(
            @PathVariable(value = "userClientId") Long userClientId,
            @PageableDefault(size = 10, sort = "userClientTeamRole.name", direction = Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<UserClientUserClientTeamRole> assembler,
            @RequestParam(value = "userClientTeamRoleId", required = false) Long userClientTeamRoleId) {
        Page<UserClientUserClientTeamRole> page = userClientUserClientTeamRoleService
                .findByUserClientAndUserClientTeamRole(new UserClient(
                        userClientId), new UserClientTeamRole(
                        userClientTeamRoleId), pageable);
        if (page != null) {
            return new ResponseEntity<>(
                    new UserClientUserClientTeamRoleResourcePage(
                            assembler
                                    .toResource(page,
                                            userClientUserClientTeamRoleResourceAssembler),
                            page, null), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Delete all UserClientUserClientTeamRole with userClientId and
     * userClientTeamRoleId
     * 
     * @param userClientId
     *            to lookup and delete
     * @param userClientTeamRoleId
     *            to lookup and delete
     */
    @RequestMapping(value = "/user_client/{userClientId}/userClientUserClientTeamRoles", method = RequestMethod.DELETE)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER" })
    public void delete(@PathVariable Long userClientId,
            @RequestParam Long userClientTeamRoleId) {
        userClientUserClientTeamRoleService.delete(
                new UserClient(userClientId), new UserClientTeamRole(
                        userClientTeamRoleId));
    }

    /**
     * Get an UserClientUserClientTeamRoleResource by
     * userClientUserClientTeamRoleId
     * 
     * @param userClientUserClientTeamRoleId
     *            to reload
     * @return UserClientUserClientTeamRoleResource with
     *         userClientUserClientTeamRoleId
     */
    @RequestMapping(value = "/user_client_user_client_team_role/{userClientUserClientTeamRoleId}", method = RequestMethod.GET)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER" })
    public ResponseEntity<UserClientUserClientTeamRoleResource> getUserClientUserClientTeamRole(
            @PathVariable(value = "userClientUserClientTeamRoleId") Long userClientUserClientTeamRoleId) {
        UserClientUserClientTeamRole usuctr = userClientUserClientTeamRoleService
                .reload(new UserClientUserClientTeamRole(
                        userClientUserClientTeamRoleId));
        if (usuctr != null) {
            return new ResponseEntity<>(
                    userClientUserClientTeamRoleResourceAssembler
                            .toResource(usuctr),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Delete an UserClientUserClientTeamRole by userClientUserClientTeamRoleId
     * 
     * @param userClientUserClientTeamRoleId
     *            to delete
     */
    @RequestMapping(value = "/user_client_user_client_team_role/{userClientUserClientTeamRoleId}", method = RequestMethod.DELETE)
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER" })
    public void delete(@PathVariable Long userClientUserClientTeamRoleId) {
        userClientUserClientTeamRoleService
                .delete(new UserClientUserClientTeamRole(
                        userClientUserClientTeamRoleId));
    }

    /**
     * Get a page of possible UserClientUserClientTeamRole
     * 
     * @param userClientId
     *            to lookup
     * @param pageable
     *            to use
     * @param assembler
     *            to assemble resource
     * @param status
     *            to lookup
     * @param term
     *            to search
     * @return a page of possible UserClientUserClientTeamRoleResource
     * 
     */
    @RequestMapping(value = "/user_clients/{userClientId}/user_client_team_roles/associate", method = RequestMethod.GET)
    @ApiOperation(value = "finds all possible user client team roles that can be associated to a user client")
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER", "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_CREATE_NEW_USER" })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query") })
    public ResponseEntity<UserClientUserClientTeamRoleResourcePage> possible(
            @PathVariable Long userClientId,
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<UserClientUserClientTeamRole> assembler,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "term", required = false) String term,
            @RequestParam(value = "tagId", required = false) Long tagId) {
        UserClientUserClientTeamRoleSearchFilter filter = new UserClientUserClientTeamRoleSearchFilter(
                new UserClient(userClientId), null, term,
                tagId != null ? new Tag(tagId) : null);
        Page<UserClientUserClientTeamRole> page = userClientUserClientTeamRoleService
                .findPossible(filter, pageable);
        if (page != null) {
            return new ResponseEntity<>(
                    new UserClientUserClientTeamRoleResourcePage(
                            assembler
                                    .toResource(page,
                                            userClientUserClientTeamRoleResourceAssembler),
                            page, filter), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Create a new UserClientUserClientTeamRole on a UserClient for each
     * existing UserClientTeamRole in a Set
     * 
     * @param userClientId
     *            to use
     * @param userClientUserClientTeamRoles
     *            to associate
     * @return a Set of created UserClientUserClientTeamRole
     * 
     */
    @RequestMapping(value = "/user_clients/{userClientId}/user_client_team_roles/associate", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE })
    @ApiOperation("create a new UserClientUserClientTeamRole on a UserClient for each existing UserClientTeamRole in a Set")
    @RolesAllowed({ "PERM_GOD", "PERM_ADMIN_SUPER_USER", "PERM_ADMIN_USER"})
    public ResponseEntity<Set<UserClientUserClientTeamRoleResource>> associate(
            @PathVariable Long userClientId,
            @RequestBody List<UserClientUserClientTeamRole> userClientUserClientTeamRoles) {
        Set<UserClientUserClientTeamRole> added = userClientUserClientTeamRoleService
                .associate(userClientUserClientTeamRoles);
        if (added != null && !added.isEmpty()) {
            Set<UserClientUserClientTeamRoleResource> ret = new HashSet<>();
            for (UserClientUserClientTeamRole ucuctr : added) {
                ret.add(userClientUserClientTeamRoleResourceAssembler
                        .toResource(ucuctr));
            }
            return new ResponseEntity<>(ret, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
