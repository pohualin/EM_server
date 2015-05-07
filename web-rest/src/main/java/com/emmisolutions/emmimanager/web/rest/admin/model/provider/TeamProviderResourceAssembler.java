package com.emmisolutions.emmimanager.web.rest.admin.model.provider;

import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocationSaveRequest;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientProvidersResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamLocationsResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamProvidersResource;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A resource assembler for TeamProvider to TeamProviderResource
 */
@Component
public class TeamProviderResourceAssembler implements ResourceAssembler<TeamProvider, TeamProviderResource> {

    @Override
    public TeamProviderResource toResource(TeamProvider entity) {
        TeamProviderResource ret = new TeamProviderResource();
        ret.add(TeamProviderPage.createProviderByIdLink(entity.getId()));

        ret.add(addPageSizeAndSort(linkTo(methodOn(TeamLocationsResource.class)
                .list(entity.getTeam().getId(), null, null)).withRel("teamLocations")));

        ret.add(linkTo(methodOn(TeamProvidersResource.class)
                .updateTeamProvider(new TeamProviderTeamLocationSaveRequest())).withRel("updateTeamProvider"));

        ret.add(addPageSizeAndSort(linkTo(methodOn(TeamProvidersResource.class)
                .findTeamLocationsByTeamProvider(entity.getId(), null, null))
                .withRel("findTeamLocationsByTeamProvider")));

        ret.add(linkTo(methodOn(ClientProvidersResource.class)
                .findByClientIdProviderId(entity.getTeam().getClient().getId(), entity.getProvider().getId()))
                .withRel("findClientProviderByClientIdProviderId"));

        ret.add(linkTo(methodOn(TeamProvidersResource.class).getById(entity.getId())).withSelfRel());
        ret.add(TeamProviderPage.createTeamProviderTeamLocationLink(entity));
        ret.setEntity(entity);
        return ret;
    }

    private Link addPageSizeAndSort(Link toAugment) {
        UriTemplate uriTemplate = new UriTemplate(toAugment.getHref())
                .with(new TemplateVariables(
                        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
                        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED),
                        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM_CONTINUED)));
        return new Link(uriTemplate, toAugment.getRel());
    }
}
