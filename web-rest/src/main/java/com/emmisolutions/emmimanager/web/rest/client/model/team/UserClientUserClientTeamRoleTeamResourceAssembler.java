package com.emmisolutions.emmimanager.web.rest.client.model.team;

import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.web.rest.client.resource.SchedulesResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
                    ret.add(linkTo(methodOn(SchedulesResource.class)
                            .schedule(entity.getTeam().getClient().getId(), entity.getTeam().getId(), null, null, null))
                            .withRel("schedulePrograms"));
                    break;
            }
        }
        return ret;
    }

}
