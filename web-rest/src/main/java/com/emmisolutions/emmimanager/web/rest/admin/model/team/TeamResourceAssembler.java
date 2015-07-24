package com.emmisolutions.emmimanager.web.rest.admin.model.team;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.ProviderPage;
import com.emmisolutions.emmimanager.web.rest.admin.model.provider.TeamProviderPage;
import com.emmisolutions.emmimanager.web.rest.admin.resource.*;

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
        ret.add(linkTo(methodOn(ClientTeamPhoneConfigurationsResource.class).findTeamPhoneConfig(entity.getId(), null)).withRel("teamPhoneConfig"));
        ret.add(linkTo(methodOn(ClientTeamSchedulingConfigurationsResource.class).findTeamSchedulingConfig(entity.getId(), null)).withRel("teamSchedulingConfig"));
        ret.add(linkTo(methodOn(ClientTeamSelfRegConfigurationsResource.class).findByTeam(entity.getId(), null)).withRel("selfRegConfig"));

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
        ret.add(createPossibleClientLocationsLink(entity));
        ret.add(linkTo(
                methodOn(TeamLocationsResource.class)
                        .associateAllClientLocationsExcept(entity.getId(), null))
                .withRel("associateAllClientLocationsExcept"));
        ret.setEntity(entity);
        return ret;
    }
    
    public static Link createPossibleClientLocationsLink(Team team) {
        Link link = linkTo(methodOn(TeamLocationsResource.class).possible(team.getId(), null, null)).withRel("possibleClientLocations");
        UriTemplate uriTemplate = new UriTemplate(link.getHref())
            .with(new TemplateVariables(
                new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM)));
        return new Link(uriTemplate, link.getRel());
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
