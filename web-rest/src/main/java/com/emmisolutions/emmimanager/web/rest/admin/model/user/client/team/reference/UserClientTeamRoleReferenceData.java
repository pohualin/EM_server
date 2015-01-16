package com.emmisolutions.emmimanager.web.rest.admin.model.user.client.team.reference;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
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
public class UserClientTeamRoleReferenceData extends ResourceSupport {

    @XmlElement(name = "permission")
    @XmlElementWrapper(name = "permissions")
    private Set<UserClientTeamPermission> userClientPermissions;

    /**
     * Creates a set of blank UserClientPermission objects
     *
     * @param possiblePermissions to use
     */
    public UserClientTeamRoleReferenceData(Set<UserClientTeamPermission> possiblePermissions) {
        this.userClientPermissions = possiblePermissions;
        add(UserClientTeamReferenceRolePage.createReferenceRolesLink());
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
