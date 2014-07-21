package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * All permissions for the application. These permissions are determined by developers internally. Role objects
 * on the other hand are created by clients themselves.
 */
@XmlEnum(String.class)
public enum PermissionName {
    PERM_GOD, PERM_USER, PERM_CLIENT_LIST, PERM_CLIENT_VIEW, PERM_CLIENT_EDIT, PERM_CLIENT_CREATE
}
