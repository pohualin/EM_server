package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Creates a ClientTeamSelfRegConfigurationResource from a
 * ClientTeamSelfRegConfiguration
 */
@Component
public class ClientTeamSelfRegConfigurationResourceAssembler
        implements
        ResourceAssembler<ClientTeamSelfRegConfiguration, ClientTeamSelfRegConfigurationResource> {

    @Override
    public ClientTeamSelfRegConfigurationResource toResource(
            ClientTeamSelfRegConfiguration entity) {
        ClientTeamSelfRegConfigurationResource ret = new ClientTeamSelfRegConfigurationResource();

        ret.setEntity(entity);
        return ret;
    }
}
