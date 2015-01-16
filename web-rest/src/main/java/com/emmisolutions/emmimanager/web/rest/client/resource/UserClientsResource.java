package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserService;
import com.emmisolutions.emmimanager.web.rest.client.model.user.UserClientResource;
import org.springframework.hateoas.ResourceAssembler;
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
@RestController("clientUserClientsResource")
@RequestMapping(value = "/webapi-client",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class UserClientsResource {

    @Resource
    UserClientService userClientService;

    @Resource
    UserService userAdminService;

    @Resource(name = "clientLevelUserClientResourceAssembler")
    ResourceAssembler<UserClient, UserClientResource> userResourceAssembler;

    @Resource
    ResourceAssembler<UserAdmin, UserClientResource> userAdminUserClientResourceAssembler;

    /**
     * GET to retrieve authenticated user
     *
     * @return UserResource or 401 if the user is not logged in (via the PERM_ADMIN_USER annotation)
     */
    @RequestMapping(value = "/authenticated", method = RequestMethod.GET)
    @RolesAllowed({
            "PERM_GOD",
            "PERM_ADMIN_USER",
            "PERM_CLIENT_SUPER_USER",
            "PERM_CLIENT_USER"})
    public ResponseEntity<UserClientResource> authenticated() {
        UserClient userClient = userClientService.loggedIn();
        if (userClient != null) {
            // a client specific user is logged in
            return new ResponseEntity<>(userResourceAssembler.toResource(userClient), HttpStatus.OK);
        } else {
            // check to see if an administrator is logged in
            UserAdmin userAdmin = userAdminService.loggedIn();
            if (userAdmin != null) {
                return new ResponseEntity<>(userAdminUserClientResourceAssembler.toResource(userAdmin), HttpStatus.OK);
            }
        }
        // the user can't be retrieved
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


}
