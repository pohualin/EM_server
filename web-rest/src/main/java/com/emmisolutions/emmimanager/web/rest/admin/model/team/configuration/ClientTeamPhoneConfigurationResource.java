package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.*;

/**
 * HATEOAS wrapper for ClientTeamPhoneConfiguration, essentially a DTO instead of
 * a wrapper.
 */
@XmlRootElement(name = "client-team-phone-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientTeamPhoneConfigurationResource extends BaseResource<ClientTeamPhoneConfiguration> {
}
