package com.emmisolutions.emmimanager.web.rest.admin.model.user.client.reference;

import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

/**
 * Reference data for UserClientRole pages
 */
@XmlRootElement(name = "reference-data")
public class UserClientRoleReferenceData extends ResourceSupport {

    @XmlElement(name = "permission")
    @XmlElementWrapper(name = "permissions")
    private Set<UserClientPermission> userClientPermissions;

    /**
     * Creates a set of blank UserClientPermission objects
     *
     * @param possiblePermissions to wrap
     */
    public UserClientRoleReferenceData(Set<UserClientPermission> possiblePermissions) {
        this.userClientPermissions = possiblePermissions;
        add(UserClientReferenceRolePage.createReferenceRolesLink());
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

}
