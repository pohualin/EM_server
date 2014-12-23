package com.emmisolutions.emmimanager.web.rest.model.user;

import java.util.ArrayList;
import java.util.List;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermission;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminUserAdminRole;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * This assembler is to be used when a User is required for a relationship with another object.
 * As such, it contains no links, and has no permissions in it.
 */
@Component("userResourceForAssociationsAssembler")
public class UserResourceForAssociationsAssembler implements ResourceAssembler<UserAdmin, UserResource> {

    @Override
    public UserResource toResource(UserAdmin user) {
        List<UserAdminPermissionName> roles = new ArrayList<>();
        for (UserAdminUserAdminRole role : user.getRoles()) {
            for (UserAdminPermission permission : role.getUserAdminRole().getPermissions()) {
                roles.add(permission.getName());
            }
        }
        
        return new UserResource(
                user.getId(),
                user.getVersion(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.isActive(),
                roles);
    }

}
