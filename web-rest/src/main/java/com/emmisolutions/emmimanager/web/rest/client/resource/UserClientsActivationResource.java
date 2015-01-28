package com.emmisolutions.emmimanager.web.rest.client.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * Api for UserClient activation
 */
@RestController
@RequestMapping(value = "/webapi-client",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class UserClientsActivationResource {

    /**
     * GET to activate a user
     *
     * @return OK
     */
    @RequestMapping(value = "/activate/{code}", method = RequestMethod.GET)
    @PermitAll
    public ResponseEntity<Void> activate(@PathVariable String code) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
