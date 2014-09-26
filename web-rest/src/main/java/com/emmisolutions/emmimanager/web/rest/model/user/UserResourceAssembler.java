package com.emmisolutions.emmimanager.web.rest.model.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.emmisolutions.emmimanager.web.rest.resource.*;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.core.AnnotationMappingDiscoverer;
import org.springframework.hateoas.core.DummyInvocationUtils;
import org.springframework.hateoas.core.MappingDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.emmisolutions.emmimanager.model.Permission;
import com.emmisolutions.emmimanager.model.PermissionName;
import com.emmisolutions.emmimanager.model.Role;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientPage;
import com.emmisolutions.emmimanager.web.rest.model.groups.ReferenceGroupPage;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationPage;
import com.emmisolutions.emmimanager.web.rest.model.team.TeamPage;

/**
 * Creates a UserResource from a User
 */
@Component
public class UserResourceAssembler implements ResourceAssembler<User, UserResource> {

    @Override
    public UserResource toResource(User user) {
        List<PermissionName> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            for (Permission permission : role.getPermissions()) {
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
        ret.add(createGroupByClientIdLink());
        ret.add(ReferenceGroupPage.createGroupReferenceDataLink());
        ret.add(TeamPage.createFullSearchLink());
        ret.add(createTeamByClientIdLink());
        ret.add(createTeamByTeamIdLink());
        ret.add(createTeamTagAssociationLink());
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
    
    
    public Link createGroupByClientIdLink() {
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) methodOn(GroupsResource.class).listGroupsByClientID(null, null, null, 1l);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("groupsByClientID");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(GroupsResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(GroupsResource.class, method),
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
    
    public Link createTeamByTeamIdLink() {
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) methodOn(TeamsResource.class).getTeam(1l);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("teamByTeamId");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(ClientsResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(ClientsResource.class, method),
                    link.getRel());
        }
        return null;
    }
    
    public Link createTeamTagAssociationLink() {
        DummyInvocationUtils.LastInvocationAware invocations = (DummyInvocationUtils.LastInvocationAware) methodOn(TeamTagsResource.class).list(1l,null,null,null,null,null);
        Method method = invocations.getLastInvocation().getMethod();
        Link link = linkTo(invocations).withRel("teamTagAssociation");
        String href = link.getHref();
        int idx = href.indexOf(discoverer.getMapping(TeamTagsResource.class));
        if (idx != -1) {
            return new Link(
                    href.substring(0, idx) + discoverer.getMapping(TeamTagsResource.class, method),
                    link.getRel());
        }
        return null;
    }

    private static final MappingDiscoverer discoverer = new AnnotationMappingDiscoverer(RequestMapping.class);

}
