package com.emmisolutions.emmimanager.web.rest.client.resource;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.UserClientValidationEmailService;
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
 * Api for UserClient email validation
 */
@RestController
@RequestMapping(value = "/webapi-client",
        produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE}
)
public class UserClientsValidationResource {
    @Resource
    UserClientValidationEmailService userClientValidationEmailService;

    /**
     * POST to activate a user
     *
     * @param validationRequest the validation request
     * @return OK
     */
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @PermitAll
    public ResponseEntity<Void> validate(@RequestBody String validationRequest) {
            UserClient userClient = userClientValidationEmailService.validate(validationRequest);
            if (userClient != null) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.GONE);
    }
}
