package com.emmisolutions.emmimanager.web.rest.admin.model.groups;

import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.TagPage;
import com.emmisolutions.emmimanager.web.rest.admin.resource.GroupsResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
        ret.add(TagPage.createFullSearchLink(entity));
        ret.setEntity(entity);
        return ret;
    }
}
