package com.emmisolutions.emmimanager.service.security;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;

import java.util.Set;

/**
 * A utility class that creates a set of UserClientUserClientTeamRole instances
 * for every team in a client with every team permission
 */
public interface SuperUserTeamRoleGenerator {

    /**
     * Create a full set of team permissions for all teams at a client
     *
     * @param client on which to look for teams
     * @return the set of permissions
     */
    Set<UserClientUserClientTeamRole> allPermissionsOnAllTeams(Client client);
}
