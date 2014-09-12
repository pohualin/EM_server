package com.emmisolutions.emmimanager.web.rest.model.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
import com.emmisolutions.emmimanager.web.rest.resource.ClientsResource;
import com.emmisolutions.emmimanager.web.rest.resource.GroupsResource;
import com.emmisolutions.emmimanager.web.rest.resource.UsersResource;

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
        ret.add(ReferenceGroupPage.createGroupReferenceDataLink().withRel("refDataGroups"));
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

    private static final MappingDiscoverer discoverer = new AnnotationMappingDiscoverer(RequestMapping.class);

}
