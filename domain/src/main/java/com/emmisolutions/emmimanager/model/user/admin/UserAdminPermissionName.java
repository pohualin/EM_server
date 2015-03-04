package com.emmisolutions.emmimanager.model.user.admin;

import javax.xml.bind.annotation.XmlEnum;

/**
 * All permissions for the application. These permissions are determined by developers internally. Role objects
 * on the other hand are created by clients themselves.
 */
@XmlEnum(String.class)
public enum UserAdminPermissionName {
    PERM_GOD,
    PERM_ADMIN_SUPER_USER,
    PERM_ADMIN_USER,
    PERM_ADMIN_CONTRACT_OWNER
}
