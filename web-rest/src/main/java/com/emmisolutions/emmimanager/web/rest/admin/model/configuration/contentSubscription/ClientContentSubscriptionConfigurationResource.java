package com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription;

import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.*;

/**
 * HATEOAS wrapper for ClientContentSubscriptionConfiguration
 */
@XmlRootElement(name = "client-content-subscription-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientContentSubscriptionConfigurationResource extends BaseResource<ClientContentSubscriptionConfiguration> {
}
