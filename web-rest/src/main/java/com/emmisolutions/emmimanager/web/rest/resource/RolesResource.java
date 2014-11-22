package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.service.UserClientRoleService;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientRoleReferenceData;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientRoleResource;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientRoleResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.user.client.UserClientRoleResourcePage;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
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
public class RolesResource {

    @Resource
    UserClientRoleService userClientRoleService;

    @Resource
    UserClientRoleResourceAssembler userClientRoleResourceAssembler;

    /**
     * Fetch all existing roles for a client.
     *
     * @param clientId  the client
     * @param pageable  page specification
     * @param assembler to make resources
     * @return a page of UserClientRoleResource objects
     */
    @RequestMapping(value = "/clients/{clientId}/roles", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_ADMINISTER_USER_ROLES"})
    @ApiOperation(value = "finds all existing roles for a client")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<UserClientRoleResourcePage> clientRoles(
        @PathVariable Long clientId,
        @PageableDefault(size = 10, sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable,
        PagedResourcesAssembler<UserClientRole> assembler) {
        Page<UserClientRole> userClientRolePage = userClientRoleService.find(new Client(clientId), pageable);
        if (userClientRolePage.hasContent()) {
            return new ResponseEntity<>(
                new UserClientRoleResourcePage(
                    assembler.toResource(userClientRolePage, userClientRoleResourceAssembler),
                    userClientRolePage),
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Create a Role on a Client
     *
     * @param clientId       on which to create
     * @param userClientRole to be created
     * @return the saved UserClientRoleResource or 500 if there's a problem saving
     */
    @RequestMapping(value = "/clients/{clientId}/roles", method = RequestMethod.POST)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_ADMINISTER_USER_ROLES"})
    @ApiOperation(value = "create a role on a client")
    public ResponseEntity<UserClientRoleResource> createRoleOn(@PathVariable Long clientId, @RequestBody UserClientRole userClientRole) {
        userClientRole.setClient(new Client(clientId));
        UserClientRole saved = userClientRoleService.create(userClientRole);
        if (saved != null) {
            return new ResponseEntity<>(userClientRoleResourceAssembler.toResource(saved), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Load a single role
     *
     * @param id of the role
     * @return a UserClientRoleResource or NO_CONTENT
     */
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_ADMINISTER_USER_ROLES"})
    @ApiOperation(value = "load one client role by id")
    public ResponseEntity<UserClientRoleResource> get(@PathVariable Long id) {
        UserClientRole ret = userClientRoleService.reload(new UserClientRole(id));
        if (ret != null) {
            return new ResponseEntity<>(
                userClientRoleResourceAssembler.toResource(ret), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Update a client role
     *
     * @param id             to update
     * @param userClientRole the updated role
     * @return the saved UserClientRoleResource after update
     */
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.PUT)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_ADMINISTER_USER_ROLES"})
    @ApiOperation(value = "update one client role by id")
    public ResponseEntity<UserClientRoleResource> updateRole(@PathVariable Long id, @RequestBody UserClientRole userClientRole) {
        UserClientRole ret = userClientRoleService.save(userClientRole);
        if (ret != null) {
            return new ResponseEntity<>(
                userClientRoleResourceAssembler.toResource(ret), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Removes a client role by id
     *
     * @param id to remove
     */
    @RequestMapping(value = "/roles/{id}", method = RequestMethod.DELETE)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_ADMINISTER_USER_ROLES"})
    @ApiOperation(value = "removes one client role by id")
    public void remove(@PathVariable Long id) {
        userClientRoleService.remove(new UserClientRole(id));
    }

    /**
     * Load all existing permissions for a role
     *
     * @param id of the role
     * @return Set of UserClientPermission objects
     */
    @RequestMapping(value = "/roles/{id}/permissions", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_ADMINISTER_USER_ROLES"})
    @ApiOperation(value = "load permissions for a role")
    public ResponseEntity<Set<UserClientPermission>> rolePermissions(@PathVariable Long id) {
        Set<UserClientPermission> ret = userClientRoleService.loadAll(new UserClientRole(id));
        if (ret != null && !ret.isEmpty()) {
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Loads reference data for client role administration
     *
     * @return the reference data
     */
    @RequestMapping(value = "/clients/roles/referenceData", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_CLIENT_ADMINISTER_USER_ROLES"})
    @ApiOperation(value = "load reference data for client role administration")
    public UserClientRoleReferenceData referenceData() {
        return new UserClientRoleReferenceData();
    }


}
