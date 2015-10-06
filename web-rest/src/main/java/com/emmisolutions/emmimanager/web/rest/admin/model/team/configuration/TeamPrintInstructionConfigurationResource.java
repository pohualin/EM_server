package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.emmisolutions.emmimanager.model.configuration.team.TeamPrintInstructionConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.model.BaseResource;

/**
 * HATEOAS wrapper for TeamPrintInstructionConfiguration, essentially a DTO
 * instead of a wrapper.
 */
@XmlRootElement(name = "team-print-instruction-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamPrintInstructionConfigurationResource extends
        BaseResource<TeamPrintInstructionConfiguration> {
}
