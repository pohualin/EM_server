package com.emmisolutions.emmimanager.web.rest.admin.model.groups;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.web.rest.resource.ReferenceGroupsResource;

/**
 * Responsible for creating a GroupResource (which has links) from a Group
 */
@Component
public class ReferenceGroupResourceAssembler implements
		ResourceAssembler<ReferenceGroup, ReferenceGroupResource> {

	@Override
	public ReferenceGroupResource toResource(ReferenceGroup entity) {
		ReferenceGroupResource ret = new ReferenceGroupResource();
		ret.add(linkTo(methodOn(ReferenceGroupsResource.class).getAllReferenceGroups(null, null, null)).withSelfRel());
		ret.add(ReferenceTagPage.createTagsReferenceDataLink(entity.getId()));
		ret.setEntity(entity);
		return ret;
	}
}
