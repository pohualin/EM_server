package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * All permissions for the application
 */
@XmlEnum(String.class)
public enum PermissionEnum {
    PERM_GOD, PERM_USER
}
