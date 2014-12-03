package com.emmisolutions.emmimanager.web.rest.model.user;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermission;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminUserAdminRole;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientPage;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceGroupPage;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationPage;
import com.emmisolutions.emmimanager.web.rest.model.provider.ProviderPage;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamPage;
import com.emmisolutions.emmimanager.web.rest.resource.ClientsResource;
import com.emmisolutions.emmimanager.web.rest.resource.LocationsResource;
import com.emmisolutions.emmimanager.web.rest.resource.ProvidersResource;
import com.emmisolutions.emmimanager.web.rest.resource.TeamsResource;
import com.emmisolutions.emmimanager.web.rest.resource.UsersResource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.core.AnnotationMappingDiscoverer;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.hateoas.core.MappingDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a UserResource from a User
 */
@Component
public class UserResourceAssembler implements ResourceAssembler<UserAdmin, UserResource> {

    @Override
    public UserResource toResource(UserAdmin user) {
        List<UserAdminPermissionName> roles = new ArrayList<>();
        for (UserAdminUserAdminRole role : user.getRoles()) {
            for (UserAdminPermission permission : role.getUserAdminRole().getPermissions()) {
                roles.add(permission.getName());
            }
        }
        UserResource ret = new UserResource(
                user.getId(),
                user.getVersion(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                roles);
        ret.add(linkTo(methodOn(UsersResource.class).authenticated()).withSelfRel());
        ret.add(ClientPage.createFullSearchLink());
        ret.add(createClientByIdLink());
        ret.add(ClientPage.createReferenceDataLink());
        ret.add(LocationPage.createFullSearchLink());
        ret.add(LocationPage.createReferenceDataLink());
        ret.add(ReferenceGroupPage.createGroupReferenceDataLink());
        ret.add(TeamPage.createFullSearchLink());
        ret.add(createTeamByClientIdLink());
        ret.add(linkTo(methodOn(TeamsResource.class).getReferenceData()).withRel("teamsReferenceData"));
        ret.add(ProviderPage.createProviderFullSearchLink());
        ret.add(linkTo(methodOn(ProvidersResource.class).getReferenceData()).withRel("providersReferenceData"));
        ret.add(createProviderByIdLink());
        ret.add(createLocationByIdLink());
        return ret;
    }

    public Link createClientByIdLink() {
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) methodOn(ClientsResource.class).get(1l);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("clientById");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(ClientsResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(ClientsResource.class, method),
                    link.getRel());
        }
        return null;
    }
    
    public Link createProviderByIdLink() {
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) methodOn(ProvidersResource.class).getById(1l);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("providerById");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(ProvidersResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(ProvidersResource.class, method),
                    link.getRel());
        }
        return null;
    }

    public Link createLocationByIdLink() {
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) methodOn(LocationsResource.class).get(1l);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("locationById");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(LocationsResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(LocationsResource.class, method),
                    link.getRel());
        }
        return null;
    }
    
    public Link createTeamByClientIdLink() {
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) methodOn(TeamsResource.class).createTeam(1l,null);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("teamsByClientId");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(ClientsResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(ClientsResource.class, method),
                    link.getRel());
        }
        return null;
    }

    private static final MappingDiscoverer discoverer = new AnnotationMappingDiscoverer(RequestMapping.class);

}
