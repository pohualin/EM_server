package com.emmisolutions.emmimanager.model.user.client;

import javax.xml.bind.annotation.XmlEnum;

/**
 * All team level permissions in the system.
 */
@XmlEnum(String.class)
public enum UserClientTeamPermissionName {
    PERM_CLIENT_TEAM_ADD_USER,
    PERM_CLIENT_TEAM_REMOVE_USER,
    PERM_CLIENT_TEAM_MODIFY_USER_ROLE,
    PERM_CLIENT_TEAM_MODIFY_USER_METADATA,
    PERM_CLIENT_TEAM_MANAGE_EMMI
}
