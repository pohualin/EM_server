package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermissionName;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.TeamPersistence;
import com.emmisolutions.emmimanager.service.security.SuperUserTeamRoleGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@Service
public class SuperUserTeamRoleGeneratorImpl implements SuperUserTeamRoleGenerator {

    @Resource
    private TeamPersistence teamPersistence;

    public Set<UserClientUserClientTeamRole> allPermissionsOnAllTeams(Client client) {
        UserClientTeamRole userClientTeamRole = new UserClientTeamRole();
        userClientTeamRole.setUserClientTeamPermissions(new HashSet<UserClientTeamPermission>() {{
            for (UserClientTeamPermissionName userClientTeamPermissionName : UserClientTeamPermissionName.values()) {
                add(new UserClientTeamPermission(userClientTeamPermissionName));
            }
        }});
        HashSet<UserClientUserClientTeamRole> teamRoles = new HashSet<>();
        Pageable page = new PageRequest(0, 50);
        Page<Team> teamPage;
        do {
            teamPage = teamPersistence.list(page, new TeamSearchFilter(client.getId(),
                    TeamSearchFilter.TeamTagType.ALL));
            for (Team team : teamPage) {
                UserClientUserClientTeamRole userClientUserClientTeamRole = new UserClientUserClientTeamRole();
                userClientUserClientTeamRole.setTeam(team);
                userClientUserClientTeamRole.setId(team.getId()); // needs to be unique to go to the set
                userClientUserClientTeamRole.setUserClientTeamRole(userClientTeamRole);
                teamRoles.add(userClientUserClientTeamRole);
            }
            page = teamPage.nextPageable();
        } while (teamPage.hasNext());
        return teamRoles;
    }
}
