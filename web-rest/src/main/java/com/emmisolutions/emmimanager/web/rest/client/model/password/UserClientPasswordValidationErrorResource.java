package com.emmisolutions.emmimanager.web.rest.client.model.password;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService.UserClientPasswordValidationError;

/**
 * Resource to include an UserClientPasswordValidationError which contains
 * password validation error and reason
 *
 */
@XmlRootElement(name = "user-client-password-validation-error")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientPasswordValidationErrorResource extends ResourceSupport {

    private UserClientPasswordValidationError entity;

    public UserClientPasswordValidationError getEntity() {
        return entity;
    }

    public void setEntity(UserClientPasswordValidationError entity) {
        this.entity = entity;
    }

}
