package com.emmisolutions.emmimanager.web.rest.model.client;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.web.rest.resource.GroupsResource;

/**
 * Responsible for creating a GroupResource (which has links) from a Group
 */
@Component
public class GroupResourceAssembler implements
		ResourceAssembler<Group, GroupResource> {

	@Override
    public GroupResource toResource(Group entity) {
    	GroupResource ret = new GroupResource();
    	ret.add(linkTo(methodOn(GroupsResource.class).getGroupById(entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
}
