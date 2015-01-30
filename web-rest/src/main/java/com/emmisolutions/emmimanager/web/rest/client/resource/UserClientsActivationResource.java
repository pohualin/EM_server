package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.activation.ActivationRequest;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.web.rest.client.model.user.UserClientResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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

    @Resource
    UserClientService userClientService;

    @Resource(name = "userClientAuthenticationResourceAssembler")
    ResourceAssembler<User, UserClientResource> userResourceAssembler;

    /**
     * POST to activate a user
     *
     * @param activationRequest the activation request
     * @return OK
     */
    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    @PermitAll
    public ResponseEntity<Void> activate(@RequestBody ActivationRequest activationRequest) {
        UserClient userClient = userClientService.activate(activationRequest);
        if (userClient != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.GONE);
    }
}
