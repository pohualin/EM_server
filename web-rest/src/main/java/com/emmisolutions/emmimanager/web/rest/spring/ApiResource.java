package com.emmisolutions.emmimanager.web.rest.spring;

import com.emmisolutions.emmimanager.web.rest.model.PublicApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

@RestController
@RequestMapping(value = "/webapi",
        produces = {APPLICATION_XML_VALUE, APPLICATION_JSON_VALUE}
)
public class ApiResource {

    @RequestMapping(method = RequestMethod.GET)
    public PublicApi get() {
        return new PublicApi();
    }
}
