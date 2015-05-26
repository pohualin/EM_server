package com.emmisolutions.emmimanager.web.rest.client.configuration.security;

import com.emmisolutions.emmimanager.model.Team;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Check to see if a team level permission is present within the authorities.
 * <p/>
 * E.g.
 */
@Component("team")
public class TeamAuthorizationRequest extends AuthorizationRequest {

    private Long teamId;
    private Long clientId;

    private TeamAuthorizationRequest() {
    }

    private TeamAuthorizationRequest(Long teamId, Long clientId) {
        this.teamId = teamId;
        this.clientId = clientId;
    }

    /**
     * Make a new TeamAuthorizationRequest instance.
     * This method is named this way to make SPeL code look good.
     *
     * @param teamId   the team id
     * @param clientId the client for the team
     * @return a new instance of this class
     */
    public TeamAuthorizationRequest id(Long teamId, Long clientId) {
        return new TeamAuthorizationRequest(teamId, clientId);
    }

    public Team getTeam() {
        return teamId != null ? new Team(teamId) : null;
    }

    @Override
    protected boolean checkPermission(String permission, Collection<? extends GrantedAuthority> authorities) {
        boolean hasPermission = false;
        if (teamId != null) {
            String permissionToCheckFor = String.format("%s_%s_%s", permission, teamId, clientId);
            for (GrantedAuthority grantedAuthority : authorities) {
                if (permissionToCheckFor.equals(grantedAuthority.getAuthority())) {
                    hasPermission = true;
                    break;
                }
            }
        }
        return hasPermission;
    }

}
