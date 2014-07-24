package com.emmisolutions.emmimanager.web.rest.model.user;

import com.emmisolutions.emmimanager.model.Permission;
import com.emmisolutions.emmimanager.model.Role;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.web.rest.model.client.ClientPage;
import com.emmisolutions.emmimanager.web.rest.resource.UsersResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, UserResource> {

    @Override
    public UserResource toResource(User user) {
        UserResource ret = new UserResource();
        List<String> roles = new ArrayList<>();
        for (Role role : user.getRoles()) {
            for (Permission permission : role.getPermissions()) {
                roles.add(permission.getName().toString());
            }
        }
        ret.login = user.getLogin();
        ret.firstName = user.getFirstName();
        ret.lastName = user.getLastName();
        ret.email = user.getEmail();
        ret.permissions = roles;
        ret.add(linkTo(methodOn(UsersResource.class).authenticated()).withSelfRel());
        ret.add(ClientPage.createFullSearchLink());
        return ret;
    }

}
