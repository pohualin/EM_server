package com.emmisolutions.emmimanager.model.user.client.team;

import javax.xml.bind.annotation.XmlEnum;

/**
 * All client team level group permissions in the system.
 */
@XmlEnum(String.class)
public enum UserClientTeamPermissionGroupName {
    PERM_CLIENT_TEAM_MANAGE_USERS_AND_ROLES,
    PERM_CLIENT_TEAM_EMMIMANAGER
}
