package com.emmisolutions.emmimanager.web.rest.client.model.user;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermissionName;
import com.emmisolutions.emmimanager.web.rest.admin.resource.UsersResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Creates a UserResource from an admin User (UserAdmin).
 * Grants the PERM_CLIENT_SUPER_USER to this user resource.
 */
@Component
public class UserAdminUserClientResourceAssembler implements ResourceAssembler<UserAdmin, UserClientResource> {

    @Override
    public UserClientResource toResource(UserAdmin user) {
        if (user == null) {
            return null;
        }
        UserClientResource ret = new UserClientResource(
                user.getId(),
                user.getVersion(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isActive(),
                new ArrayList<UserClientPermissionName>() {{
                    add(UserClientPermissionName.PERM_CLIENT_SUPER_USER);
                }});
        ret.add(linkTo(methodOn(UsersResource.class).authenticated()).withSelfRel());
        return ret;
    }

}
