package com.emmisolutions.emmimanager.web.rest.model.provider;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.annotation.Resource;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.clientprovider.ClientProviderResource;
import com.emmisolutions.emmimanager.web.rest.resource.ClientProvidersResource;

/**
 * Responsible for creating a ClientProviderResource from a sparse ClientProvider. This
 * ResourceAssembler does not accept entities with null providers but does accept a null
 * entity id.
 */
@Component
public class TeamProviderFinderResourceAssembler implements ResourceAssembler<TeamProvider, TeamProviderResource> {

    @Resource
    ClientResourceAssembler clientResourceAssembler;

    @Resource
    ProviderResourceAssembler providerResourceAssembler;

    @Override
    public TeamProviderResource toResource(TeamProvider entity) {
    	TeamProviderResource ret = new TeamProviderResource(entity, providerResourceAssembler.toResource(entity.getProvider()));
        if (entity.getId() != null) {
            ret.add(linkTo(methodOn(ClientProvidersResource.class).view(entity.getId())).withSelfRel());
        }
        return ret;
    }

}
