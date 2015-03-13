package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.web.rest.client.model.api.ClientFacingPublicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * API REST API. This is the starting point for all clients
 * to retrieve client HATEOAS links into the application
 */
@RestController("clientApiResource")
@RequestMapping(value = "/webapi-client",
        produces = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
)
@PermitAll
public class ApiResource {

    /**
     * GET for the api.
     *
     * @return PublicApi
     */
    @RequestMapping(method = RequestMethod.GET)
    public ClientFacingPublicApi get() {
        return new ClientFacingPublicApi();
    }
}
