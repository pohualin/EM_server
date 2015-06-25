package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.*;

/**
 * HATEOAS wrapper for ClientTeamSchedulingConfiguration, essentially a DTO instead of
 * a wrapper.
 */
@XmlRootElement(name = "client-team-scheduling-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientTeamSchedulingConfigurationResource extends BaseResource<ClientTeamSchedulingConfiguration> {
}
