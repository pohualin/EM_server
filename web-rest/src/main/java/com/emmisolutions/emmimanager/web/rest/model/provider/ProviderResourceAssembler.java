package com.emmisolutions.emmimanager.web.rest.model.provider;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.Provider;

@Component
public class ProviderResourceAssembler implements ResourceAssembler<Provider, ProviderResource> {

	@Override
	public ProviderResource toResource(Provider entity){
		ProviderResource ret = new ProviderResource();
		ret.setEntity(entity);
		return ret;
	}
}
