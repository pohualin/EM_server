package com.emmisolutions.emmimanager.web.rest.client.model.client;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.TeamsResource;
import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsPasswordResource;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a ClientResource (which has links) from a Client
 */
@Component("userClientClientResourceAssembler")
public class ClientResourceAssembler implements ResourceAssembler<Client, ClientResource> {

    @Override
    public ClientResource toResource(Client entity) {
        if (entity == null) {
            return null;
        }
        ClientResource ret = new ClientResource();
        ret.add(linkTo(methodOn(UserClientsPasswordResource.class).passwordPolicy(entity.getId())).withRel("passwordPolicy"));
        // ability to load a team for a client
        ret.add(new Link(
                new UriTemplate(
                        linkTo(methodOn(TeamsResource.class).loadTeamForScheduling(entity.getId(), null)).withSelfRel().getHref())
                        .with(new TemplateVariables(
                                new TemplateVariable("teamId",
                                        TemplateVariable.VariableType.REQUEST_PARAM))), "team"));

        ret.setEntity(entity);
        return ret;
    }
}
