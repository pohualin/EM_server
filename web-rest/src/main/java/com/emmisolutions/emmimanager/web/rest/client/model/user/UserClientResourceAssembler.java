package com.emmisolutions.emmimanager.web.rest.client.model.user;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermissionName;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.web.rest.admin.resource.UsersResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a UserResource from a UserClient
 */
@Component("clientLevelUserClientResourceAssembler")
public class UserClientResourceAssembler implements ResourceAssembler<UserClient, UserClientResource> {

    @Override
    public UserClientResource toResource(UserClient user) {
        if (user == null) {
            return null;
        }
        List<UserClientPermissionName> perms = new ArrayList<>();
        for (UserClientUserClientRole role : user.getClientRoles()) {
            for (UserClientPermission permission : role.getUserClientRole().getUserClientPermissions()) {
                perms.add(permission.getName());
            }
        }
        UserClientResource ret = new UserClientResource(
                user.getId(),
                user.getVersion(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isActive(),
                perms);
        ret.add(linkTo(methodOn(UsersResource.class).authenticated()).withSelfRel());
        return ret;
    }

}
