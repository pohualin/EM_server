package com.emmisolutions.emmimanager.web.rest.admin.model.clientlocation;

import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.location.LocationResourceAssembler;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Responsible for creating a ClientLocationResource
 */
@Component
public class LocationClientResourceAssembler implements
		ResourceAssembler<ClientLocation, ClientLocationResource> {

	@Resource
	ClientResourceAssembler clientResourceAssembler;

	@Resource
	LocationResourceAssembler locationResourceAssembler;

	@Override
	public ClientLocationResource toResource(ClientLocation entity) {
		ClientLocationResource ret = new ClientLocationResource();
		ret.setClient(clientResourceAssembler.toResource(entity.getClient()));
		ret.setLocation(null);
		return ret;
	}

}
