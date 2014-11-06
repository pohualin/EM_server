package com.emmisolutions.emmimanager.web.rest.model.clientprovider;

import com.emmisolutions.emmimanager.model.ClientProvider;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.resource.ClientProvidersResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a ClientProviderResource
 */
@Component
public class ClientProviderResourceAssembler implements ResourceAssembler<ClientProvider, ClientProviderResource> {

    @Resource
    ClientResourceAssembler clientResourceAssembler;

    @Resource
    ProviderResourceAssembler providerResourceAssembler;

    @Override
    public ClientProviderResource toResource(ClientProvider entity) {
        ClientProviderResource ret = new ClientProviderResource(entity, providerResourceAssembler.toResource(entity.getProvider()));
        ret.add(linkTo(methodOn(ClientProvidersResource.class).view(entity.getId())).withSelfRel());
        return ret;
    }

}
