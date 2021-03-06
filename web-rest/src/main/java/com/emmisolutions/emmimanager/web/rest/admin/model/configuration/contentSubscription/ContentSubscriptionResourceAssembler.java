package com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription;

import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientContentSubscriptionConfigurationsResource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a ContentSubscriptionResource from a ContentSubscription
 */
@Component("contentSubscriptionResourceAssembler")
public class ContentSubscriptionResourceAssembler implements
        ResourceAssembler<ContentSubscription, ContentSubscriptionResource> {

    @Override
    public ContentSubscriptionResource toResource(
    		ContentSubscription entity) {
        ContentSubscriptionResource ret = new ContentSubscriptionResource();
        ret.add(linkTo(methodOn(ClientContentSubscriptionConfigurationsResource.class).getContentSubscriptionList(null)).withSelfRel());
        ret.setEntity(entity);
        return ret;
    }
      
}
