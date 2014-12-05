package com.emmisolutions.emmimanager.web.rest.model.provider;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.web.rest.resource.ProvidersResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Converts Provider to ProviderResource objects.
 */
@Component
public class ProviderResourceAssembler implements ResourceAssembler<Provider, ProviderResource> {

	@Override
	public ProviderResource toResource(Provider entity){
		ProviderResource ret = new ProviderResource();
        ret.add(linkTo(methodOn(ProvidersResource.class).getById(entity.getId())).withSelfRel());
        ret.add(ProviderClientResourcePage.createCurrentClientsSearchLink(entity));
        ret.add(ProviderPage.createProviderReferenceDataLink());
		ret.setEntity(entity);
		return ret;
	}
}
