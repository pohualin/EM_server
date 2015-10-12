package com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientProgramContentInclusionsResource;

/**
 * Creates a ClientProgramContentInclusionResource from a
 * ClientProgramContentInclusion
 */
@Component
public class ClientProgramContentInclusionResourceAssembler
        implements
        ResourceAssembler<ClientProgramContentInclusion,ClientProgramContentInclusionResource> {

   	@Override
	public ClientProgramContentInclusionResource toResource(
			ClientProgramContentInclusion entity) {
   		ClientProgramContentInclusionResource ret = new ClientProgramContentInclusionResource();
   	    ret.add(linkTo(methodOn(ClientProgramContentInclusionsResource.class).get(entity.getClient().getId(), entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
	}

}
