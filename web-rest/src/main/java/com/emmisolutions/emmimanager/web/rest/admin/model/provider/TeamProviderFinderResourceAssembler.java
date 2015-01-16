package com.emmisolutions.emmimanager.web.rest.admin.model.provider;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamProvidersResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a TeamProviderResource from a sparse TeamProvider. This
 * ResourceAssembler does not accept entities with null providers but does accept a null
 * entity id.
 */
@Component
public class TeamProviderFinderResourceAssembler implements ResourceAssembler<TeamProvider, TeamProviderResource> {

    @Resource
    ProviderResourceAssembler providerResourceAssembler;

    @Override
    public TeamProviderResource toResource(TeamProvider entity) {
    	TeamProviderResource ret = new TeamProviderResource(entity, providerResourceAssembler.toResource(entity.getProvider()));
        if (entity.getId() != null) {
            ret.add(linkTo(methodOn(TeamProvidersResource.class).getById(entity.getId())).withSelfRel());
        }
        return ret;
    }

}
