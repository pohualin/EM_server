package com.emmisolutions.emmimanager.web.rest.model.clientlocation;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.annotation.Resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.resource.LocationsResource;

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
