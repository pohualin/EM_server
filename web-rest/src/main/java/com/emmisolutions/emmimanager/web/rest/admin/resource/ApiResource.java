package com.emmisolutions.emmimanager.web.rest.admin.resource;

import com.emmisolutions.emmimanager.web.rest.admin.model.api.PublicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * API REST API. This is the starting point for all clients
 * to retrieve HATEOAS links into the application
 */
@RestController
@RequestMapping(value = "/webapi",
        produces = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
)
@PermitAll
public class ApiResource {

    @Resource
    PublicApi publicApi;

    /**
     * GET for the api.
     *
     * @return PublicApi
     */
    @RequestMapping(method = RequestMethod.GET)
    public PublicApi get() {
        return publicApi.create();
    }
}
