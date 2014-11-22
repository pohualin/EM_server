package com.emmisolutions.emmimanager.web.rest.model.user.client;

import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermissionName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

/**
 * Reference data for UserClientRole pages
 */
@XmlRootElement(name = "reference-data")
public class UserClientRoleReferenceData {

    @XmlElement(name = "permission")
    @XmlElementWrapper(name = "permissions")
    private Set<UserClientPermission> userClientPermissions;

    /**
     * Creates a set of blank UserClientPermission objects
     */
    public UserClientRoleReferenceData() {
        userClientPermissions = new HashSet<>();
        for (UserClientPermissionName userClientPermissionName : UserClientPermissionName.values()) {
            userClientPermissions.add(new UserClientPermission(userClientPermissionName));
        }
    }
}
