package com.emmisolutions.emmimanager.web.rest.client.model.security;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.hateoas.ResourceSupport;

import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResource;

/**
 * HATEOAS wrapper for UserClientLoginError, essentially a DTO instead of a
 * wrapper.
 *
 */
@XmlRootElement(name = "user-client-login-failure")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientLoginErrorResource extends ResourceSupport {

    private UserClientLoginError entity;

    private ClientResource clientResource;

    public UserClientLoginError getEntity() {
        return entity;
    }

    public void setEntity(UserClientLoginError entity) {
        this.entity = entity;
    }

    public ClientResource getClientResource() {
        return clientResource;
    }

    public void setClientResource(ClientResource clientResource) {
        this.clientResource = clientResource;
    }

}
