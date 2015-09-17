package com.emmisolutions.emmimanager.web.rest.client.model.team;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static com.emmisolutions.emmimanager.web.rest.client.model.schedule.ScheduledProgramResourcePage.scheduleProgramsSearchLink;

/**
 * Converts UserClientUserClientTeamRole objects to TeamResource objects with the proper links governed by
 * the Role.
 */
@Component("userClientUserClientTeamRoleTeamResourceAssembler")
public class UserClientUserClientTeamRoleTeamResourceAssembler
        implements ResourceAssembler<UserClientUserClientTeamRole, TeamResource> {

    @Override
    public TeamResource toResource(UserClientUserClientTeamRole entity) {
        TeamResource ret = new TeamResource();
        ret.setEntity(entity.getTeam());
        for (UserClientTeamPermission userClientTeamPermission : entity.getUserClientTeamRole().getUserClientTeamPermissions()) {
            switch (userClientTeamPermission.getName()) {
                case PERM_CLIENT_TEAM_SCHEDULE_PROGRAM:
                    ret.add(scheduleProgramsSearchLink(entity.getTeam()));
                    break;
            }
        }
        return ret;
    }

}
