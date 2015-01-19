package com.emmisolutions.emmimanager.web.rest.admin.model.groups;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.web.rest.admin.resource.GroupsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a GroupResource (which has links) from a Group
 */
@Component
public class ReferenceGroupResourceAssembler implements
		ResourceAssembler<ReferenceGroup, ReferenceGroupResource> {

	@Override
	public ReferenceGroupResource toResource(ReferenceGroup entity) {
		ReferenceGroupResource ret = new ReferenceGroupResource();
		ret.add(linkTo(methodOn(GroupsResource.class).getRefGroups(null, null, null)).withSelfRel());
		ret.setEntity(entity);
		return ret;
	}
}
