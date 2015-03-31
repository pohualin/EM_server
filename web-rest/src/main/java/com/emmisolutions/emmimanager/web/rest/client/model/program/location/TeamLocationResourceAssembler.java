package com.emmisolutions.emmimanager.web.rest.client.model.program.location;

import com.emmisolutions.emmimanager.model.TeamLocation;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * A team location resource assembler
 */
@Component("clientTeamLocationResourceAssembler")
public class TeamLocationResourceAssembler implements ResourceAssembler<TeamLocation, TeamLocationResource> {

    @Override
    public TeamLocationResource toResource(TeamLocation entity) {
        TeamLocationResource ret = new TeamLocationResource();
        ret.setEntity(entity);
        return ret;
    }
}
