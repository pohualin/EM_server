package com.emmisolutions.emmimanager.web.rest.client.model.team.configuration;

import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.*;

import java.util.List;

/**
 * HATEOAS wrapper for ClientTeamEmailConfiguration, essentially a DTO instead of
 * a wrapper.
 */
@XmlRootElement(name = "team-email-configuration-for-patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamEmailConfigurationResource extends BaseResource<ClientTeamEmailConfiguration> {
}
