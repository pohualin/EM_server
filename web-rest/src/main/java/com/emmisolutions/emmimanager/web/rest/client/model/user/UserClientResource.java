package com.emmisolutions.emmimanager.web.rest.client.model.user;

import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResource;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.joda.time.LocalDateTime;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;

import java.util.List;

/**
 * HATEOAS wrapper for User, essentially a DTO instead of a wrapper.
 */
@XmlRootElement(name = "user-client")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientResource extends ResourceSupport {

    private Long id;

    private Integer version;

    private String login;

    private String firstName;

    private String lastName;

    private String email;

    private boolean active, accountNonExpired, accountNonLocked, credentialsNonExpired, impersonated;
    
    private LocalDateTime passwordExpirationTime;

    private ClientResource clientResource;

    @XmlElement(name = "permission")
    @XmlElementWrapper(name = "permissions")
    private List<String> permissions;


    public UserClientResource() {
    }

    /**
     * Wrapper constructor with parameters we need.
     *
     * @param id                    id
     * @param version               version
     * @param login                 login
     * @param firstName             first name
     * @param lastName              last name
     * @param email                 email
     * @param accountNonExpired     account is not expired
     * @param accountNonLocked      account is not locked
     * @param credentialsNonExpired credentials are not expired
     * @param permissions           permissions
     */
    public UserClientResource(Long id,
                              Integer version,
                              String login,
                              String firstName,
                              String lastName,
                              String email,
                              boolean active,
                              boolean accountNonExpired,
                              boolean accountNonLocked,
                              boolean credentialsNonExpired,
                              ClientResource clientResource,
                              List<String> permissions,
                              boolean impersonated,
                              LocalDateTime passwordExpirationTime) {
        this.id = id;
        this.version = version;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.clientResource = clientResource;
        this.permissions = permissions;
        this.impersonated = impersonated;
        this.passwordExpirationTime = passwordExpirationTime;
    }

    /**
     * Override to change the link property name for serialization
     *
     * @return links
     */
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("link")
    public List<Link> getLinks() {
        return super.getLinks();
    }

    public ClientResource getClientResource() {
        return clientResource;
    }

    public void setClientResource(ClientResource clientResource) {
        this.clientResource = clientResource;
    }
}
