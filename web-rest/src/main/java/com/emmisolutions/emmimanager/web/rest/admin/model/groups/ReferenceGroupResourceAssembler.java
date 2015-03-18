package com.emmisolutions.emmimanager.web.rest.admin.model.groups;

import com.emmisolutions.emmimanager.model.ReferenceGroup;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ReferenceGroupsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a ReferenceGroupResource (which has links) from a ReferenceGroup
 */
@Component
public class ReferenceGroupResourceAssembler implements ResourceAssembler<ReferenceGroup, ReferenceGroupResource> {

	@Override
	public ReferenceGroupResource toResource(ReferenceGroup entity) {
		ReferenceGroupResource ret = new ReferenceGroupResource();
        ret.add(linkTo(methodOn(ReferenceGroupsResource.class).getReferenceGroup(entity.getId())).withSelfRel());
        ret.add(linkTo(methodOn(ReferenceGroupsResource.class).deletable(entity.getId())).withRel("deletable"));
        ret.add(ReferenceTagPage.createTagsReferenceDataLink(entity.getId()));
		ret.setEntity(entity);
		return ret;
	}
}
