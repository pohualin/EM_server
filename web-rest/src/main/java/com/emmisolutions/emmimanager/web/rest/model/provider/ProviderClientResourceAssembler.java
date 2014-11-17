package com.emmisolutions.emmimanager.web.rest.model.provider;

import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.resource.ClientProvidersResource;

import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a ClientProviderResource
 */
@Component
public class ProviderClientResourceAssembler implements ResourceAssembler<ClientProvider, ProviderClientResource> {

    @Resource
    ClientResourceAssembler clientResourceAssembler;

    @Resource
    ProviderResourceAssembler providerResourceAssembler;

    @Override
    public ProviderClientResource toResource(ClientProvider entity) {
        ProviderClientResource ret = new ProviderClientResource(entity, clientResourceAssembler.toResource(entity.getClient()));
        ret.add(linkTo(methodOn(ClientProvidersResource.class).view(entity.getId())).withSelfRel());
        return ret;
    }

}
