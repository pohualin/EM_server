package com.emmisolutions.emmimanager.web.rest.admin.model.user.client.reference;

import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRole;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

/**
 * Responsible for creating a UserClientReferenceRoleResource from a UserClientReferenceRole
 */
@Component
public class UserClientReferenceRoleResourceAssembler implements
    ResourceAssembler<UserClientReferenceRole, UserClientReferenceRoleResource> {

    @Override
    public UserClientReferenceRoleResource toResource(UserClientReferenceRole entity) {
        return new UserClientReferenceRoleResource(entity);
    }
}
