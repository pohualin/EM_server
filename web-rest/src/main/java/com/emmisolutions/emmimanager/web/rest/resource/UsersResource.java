package com.emmisolutions.emmimanager.web.rest.resource;

import com.emmisolutions.emmimanager.service.UserService;
import com.emmisolutions.emmimanager.web.rest.model.user.UserResource;
import com.emmisolutions.emmimanager.web.rest.model.user.UserResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;


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

}
