package com.emmisolutions.emmimanager.web.rest.admin.model.groups;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ReferenceGroupType;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ReferenceGroupsResource;

/**
 * Responsible for creating a ReferenceGroupTypeResource (which has links) from a ReferenceGroupType
 */
@Component
public class ReferenceGroupTypeResourceAssembler implements
		ResourceAssembler<ReferenceGroupType, ReferenceGroupTypeResource> {

	@Override
	public ReferenceGroupTypeResource toResource(ReferenceGroupType entity) {
		ReferenceGroupTypeResource ret = new ReferenceGroupTypeResource();
		ret.add(linkTo(methodOn(ReferenceGroupsResource.class).getAllReferenceGroupTypes(null, null, null)).withSelfRel());
		ret.setEntity(entity);
		return ret;
	}
}
