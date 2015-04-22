package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.activation.ActivationRequest;
import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService.UserClientPasswordValidationError;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.web.rest.client.model.password.UserClientPasswordValidationErrorResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    UserClientPasswordValidationService userClientPasswordValidationService;

    @Resource(name = "userClientPasswordValidationErrorResourceAssembler")
    ResourceAssembler<UserClientPasswordValidationError, UserClientPasswordValidationErrorResource>
            userClientPasswordValidationErrorResourceAssembler;

    /**
     * POST to activate a user
     *
     * @param activationRequest the activation request
     * @return OK (200): when the activation is successful
     * <p/>
     * GONE (410): when the user client isn't found
     * <p/>
     * NOT_ACCEPTABLE (406): when there are errors during activation, (e.g. bad password, existing password, etc)
     */
    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    @PermitAll
    public ResponseEntity<List<UserClientPasswordValidationErrorResource>> activate(
            @RequestBody ActivationRequest activationRequest) {

        List<UserClientPasswordValidationError> errors = userClientPasswordValidationService
                .validateRequest(activationRequest);
        if (errors.size() == 0) {
            UserClient userClient = userClientService
                    .activate(activationRequest);
            if (userClient != null) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.GONE);
        } else {
            List<UserClientPasswordValidationErrorResource> errorResources = new ArrayList<>();
            for (UserClientPasswordValidationError error : errors) {
                errorResources
                        .add(userClientPasswordValidationErrorResourceAssembler
                                .toResource(error));
            }
            return new ResponseEntity<>(errorResources,
                    HttpStatus.NOT_ACCEPTABLE);
        }

    }

    /**
     * GET to validate an activation token
     *
     * @param activationRequest the activation request
     * @return OK or GONE
     */
    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    @PermitAll
    public ResponseEntity<Void> validateActivationToken(
            @RequestBody ActivationRequest activationRequest) {

        if (userClientService.validateActivationToken(activationRequest)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.GONE);
        }
    }
}
