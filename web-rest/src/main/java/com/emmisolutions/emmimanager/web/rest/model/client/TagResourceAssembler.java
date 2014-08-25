package com.emmisolutions.emmimanager.web.rest.model.client;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.web.rest.resource.TagsResource;
/**
 * Responsible for creating a TagResource (which has links) from a Tag
 */
@Component
public class TagResourceAssembler implements
		ResourceAssembler<Tag, TagResource> {

	@Override
    public TagResource toResource(Tag entity) {
		TagResource ret = new TagResource();
    	ret.add(linkTo(methodOn(TagsResource.class).getTagById(entity.getId())).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
}
