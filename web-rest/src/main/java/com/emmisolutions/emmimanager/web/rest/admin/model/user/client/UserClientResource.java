package com.emmisolutions.emmimanager.web.rest.admin.model.user.client;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * HATEOAS wrapper for UserClient, essentially a DTO instead of a wrapper.
 */
@XmlRootElement(name = "user-client")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientResource extends ResourceSupport {

    private UserClient entity;

    private List<UserClientService.UserClientConflict> conflicts;
    
    private UserClientService.UserClientValidationError validationError;

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

    public UserClient getEntity() {
        return entity;
    }

    public void setEntity(UserClient entity) {
        this.entity = entity;
    }

    public List<UserClientService.UserClientConflict> getConflicts() {
        return conflicts;
    }

    public void setConflicts(
            List<UserClientService.UserClientConflict> conflicts) {
        this.conflicts = conflicts;
    }

    public UserClientService.UserClientValidationError getValidationError() {
        return validationError;
    }

    public void setValidationError(
            UserClientService.UserClientValidationError validationError) {
        this.validationError = validationError;
    }
}
