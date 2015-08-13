package com.emmisolutions.emmimanager.web.rest.admin.model.team.configuration;

import com.emmisolutions.emmimanager.model.ClientTeamSelfRegConfiguration;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientTeamSelfRegConfigurationsResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientsResource;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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

    public static Link findTeamBySelfRegCodeLink() {
        Link link = linkTo(methodOn(ClientTeamSelfRegConfigurationsResource.class).findByCode(null)).withRel("findBySelfRegCode");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("code", TemplateVariable.VariableType.REQUEST_PARAM)));
        return new Link(uriTemplate, link.getRel());
    }

}
