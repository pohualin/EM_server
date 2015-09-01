package com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription;

import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.*;


/**
 * HATEOAS wrapper for ContentSubscription
 */
@XmlRootElement(name = "content-subscription")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContentSubscriptionResource extends BaseResource<ContentSubscription> {
  
}
