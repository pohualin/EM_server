package com.emmisolutions.emmimanager.web.rest.model.user;

import com.emmisolutions.emmimanager.model.Permission;
import com.emmisolutions.emmimanager.model.PermissionName;
import com.emmisolutions.emmimanager.model.Role;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientPage;
import com.emmisolutions.emmimanager.web.rest.model.location.LocationPage;
import com.emmisolutions.emmimanager.web.rest.resource.UsersResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
        ret.add(ClientPage.createReferenceDataLink());
        ret.add(LocationPage.createFullSearchLink());
        ret.add(LocationPage.createReferenceDataLink());
        return ret;
    }

}
