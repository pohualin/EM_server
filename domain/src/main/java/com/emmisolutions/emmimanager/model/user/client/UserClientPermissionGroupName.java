package com.emmisolutions.emmimanager.model.user.client;

import javax.xml.bind.annotation.XmlEnum;

/**
 * All client level group permissions in the system.
 */
@XmlEnum(String.class)
public enum UserClientPermissionGroupName {
    PERM_CLIENT_SUPER_USER,
    PERM_CLIENT_MANAGE_USERS,
    PERM_CLIENT_MODIFY_CONFIG
}
