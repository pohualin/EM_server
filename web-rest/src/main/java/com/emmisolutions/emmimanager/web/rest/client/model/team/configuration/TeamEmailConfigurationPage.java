package com.emmisolutions.emmimanager.web.rest.client.model.team.configuration;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.web.rest.client.model.PagedResource;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS wrapper for a page of ClientTeamEmailConfigurationResource objects.
 */
@XmlRootElement(name = "team-email-configuration-for-patient-page")
public class TeamEmailConfigurationPage extends
        PagedResource<TeamEmailConfigurationResource> {

    public TeamEmailConfigurationPage() {
    }

    /**
     * Wrapped constructor
     * 
     * @param clientTeamEmailConfigurationResources
     *            to be wrapped
     * @param clientTeamEmailConfigurationPage
     *            the raw response
     */
    public TeamEmailConfigurationPage(
            PagedResources<TeamEmailConfigurationResource> clientTeamEmailConfigurationResources,
            Page<ClientTeamEmailConfiguration> clientTeamEmailConfigurationPage) {
        pageDefaults(clientTeamEmailConfigurationResources,
        		clientTeamEmailConfigurationPage);
    }

}
