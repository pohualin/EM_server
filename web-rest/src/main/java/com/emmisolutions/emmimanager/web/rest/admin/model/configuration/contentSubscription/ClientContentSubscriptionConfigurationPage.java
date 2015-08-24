package com.emmisolutions.emmimanager.web.rest.admin.model.configuration.contentSubscription;

import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.web.rest.client.model.PagedResource;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS wrapper for a page of ClientContentSubscriptionConfigurationResource objects.
 */
@XmlRootElement(name = "client-content-subscription-configuration-page")
public class ClientContentSubscriptionConfigurationPage extends
        PagedResource<ClientContentSubscriptionConfigurationResource> {

    public ClientContentSubscriptionConfigurationPage() {
    }

    /**
     * Wrapped constructor
     * 
     * @param clientContentSubscritpionConfigurationResources
     *            to be wrapped
     * @param clientContentSubscriptionConfigurationPage
     *            the raw response
     */
    public ClientContentSubscriptionConfigurationPage(
            PagedResources<ClientContentSubscriptionConfigurationResource> clientContentSubscriptionConfigurationResources,
            Page<ClientContentSubscriptionConfiguration> clientContentSubscriptionConfigurationPage) {
        pageDefaults(clientContentSubscriptionConfigurationResources,
        		clientContentSubscriptionConfigurationPage);
    }

}
