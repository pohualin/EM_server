package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * HATEOAS wrapper for ClientTeamSelfRegConfiguration, essentially a DTO instead of
 * a wrapper.
 */
@XmlRootElement(name = "client-team-self-reg-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientTeamSelfRegConfigurationResource extends BaseResource<ClientTeamSelfRegConfiguration> {
}
