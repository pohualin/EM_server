package com.emmisolutions.emmimanager.web.rest.model.user.client.team;

import com.emmisolutions.emmimanager.model.user.client.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.UserClientTeamPermissionName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

/**
 * Reference data for UserClientTeamRole pages
 */
@XmlRootElement(name = "reference-data")
public class UserClientTeamRoleReferenceData {

    @XmlElement(name = "permission")
    @XmlElementWrapper(name = "permissions")
    private Set<UserClientTeamPermission> userClientTeamPermissions;

    /**
     * Creates a set of blank UserClientPermission objects
     */
    public UserClientTeamRoleReferenceData() {
        userClientTeamPermissions = new HashSet<>();
        for (UserClientTeamPermissionName userClientTeamPermissionName : UserClientTeamPermissionName.values()) {
            userClientTeamPermissions.add(new UserClientTeamPermission(userClientTeamPermissionName));
        }
    }
}
