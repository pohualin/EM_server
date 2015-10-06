package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import com.emmisolutions.emmimanager.model.configuration.team.TeamPrintInstructionConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamPrintInstructionConfigurationsResource;

/**
 * Creates a TeamPrintInstructionConfigurationResource from a
 * TeamPrintInstructionConfiguration
 */
@Component
public class TeamPrintInstructionConfigurationResourceAssembler
        implements
        ResourceAssembler<TeamPrintInstructionConfiguration, TeamPrintInstructionConfigurationResource> {

    @Override
    public TeamPrintInstructionConfigurationResource toResource(
            TeamPrintInstructionConfiguration entity) {
        TeamPrintInstructionConfigurationResource ret = new TeamPrintInstructionConfigurationResource();
        if (entity.getId() != null) {
            ret.add(linkTo(
                    methodOn(TeamPrintInstructionConfigurationsResource.class)
                            .get(entity.getId())).withSelfRel());
        }
        ret.setEntity(entity);
        return ret;
    }
}
