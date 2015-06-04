package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.web.rest.client.model.PagedResource;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedResources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HATEOAS wrapper for a page of ClientTeamPhoneConfigurationResource objects.
 */
@XmlRootElement(name = "client-team-phone-configuration-page")
public class ClientTeamPhoneConfigurationPage extends
        PagedResource<ClientTeamPhoneConfigurationResource> {

    public ClientTeamPhoneConfigurationPage() {
    }

    /**
     * Wrapped constructor
     * 
     * @param clientTeamPhoneConfigurationResources
     *            to be wrapped
     * @param clientTeamPhoneConfigurationPage
     *            the raw response
     */
    public ClientTeamPhoneConfigurationPage(
            PagedResources<ClientTeamPhoneConfigurationResource> clientTeamPhoneConfigurationResources,
            Page<ClientTeamPhoneConfiguration> clientTeamPhoneConfigurationPage) {
        pageDefaults(clientTeamPhoneConfigurationResources,
        		clientTeamPhoneConfigurationPage);
    }

}
