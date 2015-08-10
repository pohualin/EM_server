package com.emmisolutions.emmimanager.web.rest.client.model.team.configuration;

import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.*;

/**
 * HATEOAS wrapper for TeamSchedulingConfiguration, essentially a DTO instead of
 * a wrapper.
 */
@XmlRootElement(name = "team-scheduling-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamSchedulingConfigurationResource extends BaseResource<ClientTeamSchedulingConfiguration> {
}
