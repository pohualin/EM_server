package com.emmisolutions.emmimanager.web.rest.admin.model.team;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.ProviderPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.TeamProviderPage;
import com.emmisolutions.emmimanager.web.rest.admin.resource.ClientTeamEmailConfigurationsResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamLocationsResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamProvidersResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamTagsResource;
import com.emmisolutions.emmimanager.web.rest.admin.resource.TeamsResource;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Responsible for creating a TeamResource (which has links) from a Team
 */
@Component
public class TeamResourceAssembler implements ResourceAssembler<Team, TeamResource> {

    @Override
    public TeamResource toResource(Team entity) {
        TeamResource ret = new TeamResource();
        ret.add(linkTo(methodOn(TeamsResource.class).getTeam(entity.getClient().getId(), entity.getId())).withSelfRel());

        ret.add(ProviderPage.createProviderReferenceDataLink().withRel("providerReferenceData"));
        ret.add(ProviderPage.createProviderLink(entity.getClient().getId(), entity.getId()).withRel("provider"));
       
        ret.add(linkTo(methodOn(ClientTeamEmailConfigurationsResource.class).findTeamEmailConfig(entity.getId(), null, null)).withRel("teamEmailConfig"));

        ret.add(addPageSizeAndSort(
                linkTo(methodOn(TeamTagsResource.class).list(entity.getId(), null, null)).withRel("tags")));

        ret.add(addPageSizeAndSort(
                linkTo(methodOn(TeamLocationsResource.class)
                        .list(entity.getId(), null, null)).withRel("teamLocations")));

        ret.add(addPageSizeAndSort(
                linkTo(methodOn(TeamProvidersResource.class)
                        .list(entity.getId(), null, null)).withRel("teamProviders")));
        ret.add(TeamProviderPage.createAssociationLink(entity));
        ret.add(TeamResource.getByProviderAndTeam(entity));
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
