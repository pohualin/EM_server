package com.emmisolutions.emmimanager.model.user.client.team;

import javax.xml.bind.annotation.XmlEnum;

/**
 * All team level permissions in the system.
 */
@XmlEnum(String.class)
public enum UserClientTeamPermissionName {
    PERM_CLIENT_TEAM_MODIFY_USER_ROLE,
    PERM_CLIENT_TEAM_MODIFY_USER_METADATA,
    PERM_CLIENT_TEAM_SCHEDULE_PROGRAM,
    PERM_CLIENT_TEAM_PATIENT_LIST
}
