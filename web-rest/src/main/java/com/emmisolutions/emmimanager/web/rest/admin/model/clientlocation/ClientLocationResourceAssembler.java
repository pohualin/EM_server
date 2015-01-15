package com.emmisolutions.emmimanager.web.rest.admin.model.clientlocation;

import com.emmisolutions.emmimanager.model.ClientLocation;
import com.emmisolutions.emmimanager.web.rest.admin.model.client.ClientResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.model.location.LocationResourceAssembler;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientLocationsResource;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a ClientLocationResource
 */
@Component
public class ClientLocationResourceAssembler implements ResourceAssembler<ClientLocation, ClientLocationResource> {

    @Resource
    ClientResourceAssembler clientResourceAssembler;

    @Resource
    LocationResourceAssembler locationResourceAssembler;

    @Override
    public ClientLocationResource toResource(ClientLocation entity) {
        ClientLocationResource ret = new ClientLocationResource();
        ret.add(linkTo(methodOn(ClientLocationsResource.class).view(entity.getClient().getId(), entity.getId())).withSelfRel());
        ret.add(createTeamsSearchLink(entity));
        ret.setClient(null); // don't need to send up the client
        ret.setLocation(locationResourceAssembler.toResource(entity.getLocation()));
        return ret;
    }

    /**
     * Create the search link to find teams used by this ClientLocation
     *
     * @param entity to search for
     * @return Link for team searches from the ClientLocation
     */
    public Link createTeamsSearchLink(ClientLocation entity) {
        Link link = linkTo(methodOn(ClientLocationsResource.class).teams(entity.getClient().getId(), entity.getId(), null, null)).withRel("teams");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, link.getRel());
    }

}
