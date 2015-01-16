package com.emmisolutions.emmimanager.web.rest.client.model.security;

import com.emmisolutions.emmimanager.model.Team;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Check to see if a team level permission is present within the authorities.
 *
 * E.g.
 *
 */
@Component("team")
public class TeamAuthorizationRequest extends AuthorizationRequest{

    private Long teamId;

    private TeamAuthorizationRequest() {
    }

    private TeamAuthorizationRequest(Long teamId) {
        this.teamId = teamId;
    }

    public TeamAuthorizationRequest _new(Long teamId) {
        return new TeamAuthorizationRequest(teamId);
    }

    public Team getTeam() {
        return teamId != null ? new Team(teamId) : null;
    }

    @Override
    protected boolean checkPermission(String permission, Collection<? extends GrantedAuthority> authorities) {
        boolean hasPermission = false;
        if (teamId != null) {
            String permissionToCheckFor = permission + "_" + teamId;
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
