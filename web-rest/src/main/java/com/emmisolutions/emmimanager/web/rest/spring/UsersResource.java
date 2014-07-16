package com.emmisolutions.emmimanager.web.rest.spring;

import com.emmisolutions.emmimanager.service.UserService;
import com.emmisolutions.emmimanager.web.rest.model.UserDetailsResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;


@RestController
@RequestMapping(value = "/webapi",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class UsersResource {

    @Resource
    UserService userService;

    @RequestMapping(value = "/authenticated", method = RequestMethod.GET)
    public ResponseEntity<UserDetailsResource> authenticated() {
        return new ResponseEntity<>(new UserDetailsResource(userService.loggedIn()), HttpStatus.OK);
    }

}
