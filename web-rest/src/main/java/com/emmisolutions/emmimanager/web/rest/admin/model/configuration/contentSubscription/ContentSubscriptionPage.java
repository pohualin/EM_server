package com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription;

import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.web.rest.admin.model.PagedResource;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS wrapper for a page of ContentSubscriptionResource objects.
 */
@XmlRootElement(name = "contentSubscriptionPage")
public class ContentSubscriptionPage extends PagedResource<ContentSubscriptionResource> {

    public ContentSubscriptionPage() {
    }

    /**
     * Wrapper for Content Subscription resource objects
     *
     * @param contentSubscriptionResource to be wrapped
     * @param contentSubscriptionPage true page
     */
    public ContentSubscriptionPage(
        PagedResources<ContentSubscriptionResource> contentSubscriptionResource, 
        Page<ContentSubscription> contentSubscriptionPage) {
        pageDefaults(contentSubscriptionResource, contentSubscriptionPage);
    }

     
  
}
