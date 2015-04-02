package com.emmisolutions.emmimanager.web.rest.client.model.program.provider;

import com.emmisolutions.emmimanager.model.TeamProvider;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * A team provider resource assembler
 */
@Component("clientTeamProviderResourceAssembler")
public class TeamProviderResourceAssembler implements ResourceAssembler<TeamProvider, TeamProviderResource> {

    @Override
    public TeamProviderResource toResource(TeamProvider entity) {
        TeamProviderResource ret = new TeamProviderResource();
        ret.setEntity(entity);
        return ret;
    }
}
