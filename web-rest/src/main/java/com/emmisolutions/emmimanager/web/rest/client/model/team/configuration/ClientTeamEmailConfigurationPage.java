package com.emmisolutions.emmimanager.web.rest.client.model.team.configuration;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.web.rest.client.model.PagedResource;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS wrapper for a page of ClientTeamEmailConfigurationResource objects.
 */
@XmlRootElement(name = "client-team-email-configuration-page")
public class ClientTeamEmailConfigurationPage extends
        PagedResource<ClientTeamEmailConfigurationResource> {

    public ClientTeamEmailConfigurationPage() {
    }

    /**
     * Wrapped constructor
     * 
     * @param emailRestrictConfigurationResources
     *            to be wrapped
     * @param emailRestrictConfigurationPage
     *            the raw response
     */
    public ClientTeamEmailConfigurationPage(
            PagedResources<ClientTeamEmailConfigurationResource> clientTeamEmailConfigurationResources,
            Page<ClientTeamEmailConfiguration> clientTeamEmailConfigurationPage) {
        pageDefaults(clientTeamEmailConfigurationResources,
        		clientTeamEmailConfigurationPage);
    }

}
