package com.emmisolutions.emmimanager.web.rest.admin.model.groups;

import com.emmisolutions.emmimanager.model.ReferenceTag;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ProvidersResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a ReferenceTagResource (which has links) from a ReferenceTag
 */
@Component
public class ReferenceTagResourceAssembler implements
		ResourceAssembler<ReferenceTag, ReferenceTagResource> {

	@Override
	public ReferenceTagResource toResource(ReferenceTag entity) {
		ReferenceTagResource ret = new ReferenceTagResource();
		ret.add(linkTo(methodOn(ProvidersResource.class).getRefData(null, null)).withSelfRel());
		ret.setEntity(entity);
		return ret;
	}
}
