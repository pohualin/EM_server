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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emmisolutions.emmimanager.model.UserAdminSaveRequest;
import com.emmisolutions.emmimanager.model.UserAdminSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.service.UserService;
import com.emmisolutions.emmimanager.web.rest.model.user.UserAdminRolePage;
import com.emmisolutions.emmimanager.web.rest.model.user.UserAdminRoleResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.user.UserPage;
import com.emmisolutions.emmimanager.web.rest.model.user.UserResource;
import com.emmisolutions.emmimanager.web.rest.model.user.UserResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.user.UserResourceForAssociationsAssembler;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;


/**
 * User REST API
 */
@RestController
@RequestMapping(value = "/webapi",
    produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class UsersResource {

    @Resource
    UserService userService;

    @Resource
    UserResourceAssembler userResourceAssembler;

    @Resource
    UserAdminRoleResourceAssembler userAdminRoleResourceAssembler;
    
    @Resource 
    UserResourceForAssociationsAssembler userAdminResourceAssembler;
    
    /**
     * GET to retrieve authenticated user
     *
     * @return UserResource or 401 if the user is not logged in (via the PERM_ADMIN_USER annotation)
     */
    @RequestMapping(value = "/authenticated", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    public ResponseEntity<UserResource> authenticated() {
        return new ResponseEntity<>(userResourceAssembler.toResource(userService.loggedIn()), HttpStatus.OK);
    }
    
    /**
     * Get a page of User that satisfy the search criteria
     *
     * @param pageable  to use
     * @param sort      to use
     * @param assembler to use
     * @param status    to filter
     * @param term      to search
     * @return UserPage
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_SUPER_USER",
    "PERM_CREATE_NEW_USER"})
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "sort", defaultValue = "lastName,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")})
    public ResponseEntity<UserPage> list(
            @PageableDefault(size = 10, sort = "lastName", direction = Direction.ASC) Pageable pageable,
            PagedResourcesAssembler<UserAdmin> assembler,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "term", required = false) String term) {

        UserAdminSearchFilter filter = new UserAdminSearchFilter(UserAdminSearchFilter.StatusFilter.fromStringOrActive(status) , term);

        Page<UserAdmin> users = userService.list(pageable, filter);

        if (users.hasContent()) {
            // create a ClientPage containing the response
            return new ResponseEntity<>(new UserPage(assembler.toResource(users, userAdminResourceAssembler), users),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }    
    
    /**
     * Create a brand new User
     *
     * @param user to create
     * @return User created
     */
    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_SUPER_USER",
            "PERM_CREATE_NEW_USER"})
    public ResponseEntity<UserResource> createUser(@RequestBody UserAdminSaveRequest req) {

    	UserAdmin savedUser = userService.save(req);
        if (savedUser != null) {
            // created a user client successfully
            return new ResponseEntity<>(
            		userAdminResourceAssembler.toResource(savedUser),
                    HttpStatus.CREATED);
        } else {
            // error creating user client
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }  	
    }

    /**
     * Create a brand new User
     *
     * @param user to create
     * @return User created
     */
    @RequestMapping(value = "/users", method = RequestMethod.PUT, consumes = {
            APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE})
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_SUPER_USER",
            "PERM_CREATE_NEW_USER"})
    public ResponseEntity<UserResource> updateUser(@RequestBody UserAdminSaveRequest req) {

    	UserAdmin savedUser = userService.save(req);
    	savedUser = userService.fetchUserWillFullPermissions(savedUser);
        if (savedUser != null) {
            // created a user client successfully
            return new ResponseEntity<>(
                    userResourceAssembler.toResource(savedUser),
                    HttpStatus.CREATED);
        } else {
            // error creating user client
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }  	
    }
    
    /**
     * GET a single user
     *
     * @param id       to load
     * @return ClientResource or NO_CONTENT
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER", "PERM_SUPER_USER",
    "PERM_CREATE_NEW_USER"})
    public ResponseEntity<UserResource> get(@PathVariable("id") Long id) {
        UserAdmin toFind = new UserAdmin();
        toFind.setId(id);
        toFind = userService.fetchUserWillFullPermissions(toFind);
        if (toFind != null) {
            return new ResponseEntity<>(userResourceAssembler.toResource(toFind), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    /**
     * Fetch all existing user admin roles 
     *
     * @param clientId  the client
     * @param pageable  page specification
     * @param assembler to make resources
     * @return a page of UserClientRoleResource objects
     */
    @RequestMapping(value = "/admin/roles", method = RequestMethod.GET)
    @RolesAllowed({"PERM_GOD", "PERM_ADMIN_USER"})
    @ApiOperation(value = "finds all existing user admin roles ")
    @ApiImplicitParams(value = {
        @ApiImplicitParam(name = "size", defaultValue = "10", value = "number of items on a page", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "page", defaultValue = "0", value = "page to request (zero index)", dataType = "integer", paramType = "query"),
        @ApiImplicitParam(name = "sort", defaultValue = "name,asc", value = "sort to apply format: property,asc or desc", dataType = "string", paramType = "query")
    })
    public ResponseEntity<UserAdminRolePage> userAdminRoles(
        @PageableDefault(size = 10, sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable,
        PagedResourcesAssembler<UserAdminRole> assembler) {
    	
        Page<UserAdminRole> userRolePage = userService.listRolesWithoutSystem(pageable);
        if (userRolePage.hasContent()) {
            return new ResponseEntity<>(
                new UserAdminRolePage(
                    assembler.toResource(userRolePage, userAdminRoleResourceAssembler),
                    userRolePage),
                HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
