package com.emmisolutions.emmimanager.model.user.client;

import javax.xml.bind.annotation.XmlEnum;

/**
 * All client level permissions in the system.
 */
@XmlEnum(String.class)
public enum UserClientPermissionName {
    PERM_CLIENT_SUPER_USER,
    PERM_CLIENT_MANAGE_ROLES,
    PERM_CLIENT_MANAGE_USERS,
    PERM_CLIENT_MODIFY_PASSWORD_CONFIG,
    PERM_CLIENT_MODIFY_SECURITY_CONFIG
}
