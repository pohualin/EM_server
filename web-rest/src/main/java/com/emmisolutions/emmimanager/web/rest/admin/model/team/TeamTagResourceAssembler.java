package com.emmisolutions.emmimanager.web.rest.admin.model.team;

import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamTagsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a TeamTagResource (which has links) from a TeamTag
 */
@Component
public class TeamTagResourceAssembler implements ResourceAssembler<TeamTag, TeamTagResource> {

    @Override
    public TeamTagResource toResource(TeamTag entity) {
        TeamTagResource ret = new TeamTagResource();
        ret.add(linkTo(methodOn(TeamTagsResource.class).getTeamTag(entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
}