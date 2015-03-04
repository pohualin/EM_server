package com.emmisolutions.emmimanager.web.rest.client.model.security;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

/**
 * HATEOAS wrapper for UserClientLoginError, essentially a DTO instead of a
 * wrapper.
 *
 */
@XmlRootElement(name = "user-client-login-failure")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientLoginErrorResource extends ResourceSupport {

    private UserClientLoginError entity;

    public UserClientLoginError getEntity() {
        return entity;
    }

    public void setEntity(UserClientLoginError entity) {
        this.entity = entity;
    }

}
