package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRole;
import com.emmisolutions.emmimanager.service.UserClientTeamRoleService;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.UserClientTeamRoleResource;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.UserClientTeamRoleResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.UserClientTeamRoleResourcePage;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.reference.UserClientTeamReferenceRolePage;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.reference.UserClientTeamReferenceRoleResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.reference.UserClientTeamRoleReferenceData;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Roles/Permissions REST API
 */
@RestController
@RequestMapping(value = "/webapi",
    produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class ClientTeamRolesAdminResource {

    @Resource
    UserClientTeamRoleService userClientTeamRoleService;

    @Resource
    UserClientTeamRoleResourceAssembler userClientTeamRoleResourceAssembler;

    @Resource
    UserClientTeamReferenceRoleResourceAssembler userClientTeamReferenceRoleResourceAssembler;

    /**
     * Fetch all existing roles for a client.
     *
     * @param clientId  the client
     * @param pageable  page specification
     * @param assembler to make resources
     * @return a page of UserClientTeamRoleResource objects
     */
    @RequestMapping(value = "/clients/{clientId}/admin/team-roles", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    @ApiOperation(value = "finds all existing team roles for a client")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<UserClientTeamRoleResourcePage> clientTeamRoles(
        @PathVariable Long clientId,
        @PageableDefault(size = 10, sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable,
        PagedResourcesAssembler<UserClientTeamRole> assembler) {
        Page<UserClientTeamRole> userClientTeamRolePage = userClientTeamRoleService.find(new Client(clientId), pageable);
        if (userClientTeamRolePage.hasContent()) {
            return new ResponseEntity<>(
                new UserClientTeamRoleResourcePage(
                    assembler.toResource(userClientTeamRolePage, userClientTeamRoleResourceAssembler),
                    userClientTeamRolePage),
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Create a team role on a Client
     *
     * @param clientId           on which to create
     * @param userClientTeamRole to be created
     * @return the saved UserClientTeamRoleResource or 500 if there's a problem saving
     */
    @RequestMapping(value = "/clients/{clientId}/admin/team-roles", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    @ApiOperation(value = "create a role on a client")
    public ResponseEntity<UserClientTeamRoleResource> createRoleOn(@PathVariable Long clientId, @RequestBody UserClientTeamRole userClientTeamRole) {
        userClientTeamRole.setClient(new Client(clientId));
        UserClientTeamRole saved = userClientTeamRoleService.create(userClientTeamRole);
        if (saved != null) {
            return new ResponseEntity<>(userClientTeamRoleResourceAssembler.toResource(saved), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Load a single role
     *
     * @param id of the role
     * @return a UserClientTeamRoleResource or NO_CONTENT
     */
    @RequestMapping(value = "/admin/team-roles/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    @ApiOperation(value = "load one client role by id")
    public ResponseEntity<UserClientTeamRoleResource> get(@PathVariable Long id) {
        UserClientTeamRole ret = userClientTeamRoleService.reload(new UserClientTeamRole(id));
        if (ret != null) {
            return new ResponseEntity<>(
                userClientTeamRoleResourceAssembler.toResource(ret), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Update a team role
     *
     * @param id                 to update
     * @param userClientTeamRole the updated role
     * @return the saved UserClientTeamRoleResource after update
     */
    @RequestMapping(value = "/admin/team-roles/{id}", method = RequestMethod.PUT)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    @ApiOperation(value = "update one client role by id")
    public ResponseEntity<UserClientTeamRoleResource> updateRole(@PathVariable Long id, @RequestBody UserClientTeamRole userClientTeamRole) {
        UserClientTeamRole ret = userClientTeamRoleService.update(userClientTeamRole);
        if (ret != null) {
            return new ResponseEntity<>(
                userClientTeamRoleResourceAssembler.toResource(ret), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Removes a client role by id
     *
     * @param id to remove
     */
    @RequestMapping(value = "/admin/team-roles/{id}", method = RequestMethod.DELETE)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    @ApiOperation(value = "removes one client role by id")
    public void remove(@PathVariable Long id) {
        userClientTeamRoleService.remove(new UserClientTeamRole(id));
    }

    /**
     * Load all existing permissions for a role
     *
     * @param id of the role
     * @return Set of UserClientPermission objects
     */
    @RequestMapping(value = "/admin/team-roles/{id}/permissions", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    @ApiOperation(value = "load permissions for a role")
    public ResponseEntity<Set<UserClientTeamPermission>> rolePermissions(@PathVariable Long id) {
        Set<UserClientTeamPermission> ret = userClientTeamRoleService.loadAll(new UserClientTeamRole(id));
        if (ret != null && !ret.isEmpty()) {
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Loads reference data for team role administration
     *
     * @return the reference data
     */
    @RequestMapping(value = "/admin/team-roles/reference", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    @ApiOperation(value = "load reference data for client role administration")
    public UserClientTeamRoleReferenceData referenceData() {
        return new UserClientTeamRoleReferenceData(userClientTeamRoleService.loadPossiblePermissions());
    }

    /**
     * GET to retrieve UserClientReferenceRolePage data.
     *
     * @param pageable  paged request
     * @param sort      sorting request
     * @param assembler used to create PagedResources
     * @return a page of template roles matching the search request
     */
    @RequestMapping(value = "/admin/team-roles/reference/roles", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    @ApiOperation(value = "finds all existing roles for a client")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "id,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<UserClientTeamReferenceRolePage> referenceRoles(@PageableDefault(size = 50) Pageable pageable,
                                                                          @SortDefault(sort = "id") Sort sort,
                                                                          PagedResourcesAssembler<UserClientReferenceTeamRole> assembler) {
        Page<UserClientReferenceTeamRole> referenceRolePage = userClientTeamRoleService.loadReferenceRoles(pageable);
        if (referenceRolePage.hasContent()) {
            return new ResponseEntity<>(
                new UserClientTeamReferenceRolePage(assembler.toResource(referenceRolePage,
                    userClientTeamReferenceRoleResourceAssembler), referenceRolePage), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

}
