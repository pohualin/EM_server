package com.emmisolutions.emmimanager.web.rest.model.user.client.team;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * Reference data for UserClientTeamRole pages
 */
@XmlRootElement(name = "reference-data")
public class UserClientTeamRoleReferenceData extends ResourceSupport {

    @XmlElement(name = "permission")
    @XmlElementWrapper(name = "permissions")
    private Set<UserClientTeamPermission> userClientTeamPermissions;

    /**
     * Creates a set of blank UserClientPermission objects
     */
    public UserClientTeamRoleReferenceData(Set<UserClientTeamPermission> possiblePermissions) {
        this.userClientTeamPermissions = possiblePermissions;
    }

}
