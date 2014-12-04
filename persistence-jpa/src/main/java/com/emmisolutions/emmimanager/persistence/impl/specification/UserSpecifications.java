package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.user.admin.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;

import static com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName.PERM_ADMIN_CONTRACT_OWNER;

/**
 * Specifications for a User.
 */
@Component
public class UserSpecifications {

    /**
     * Filter to ensure User is a contract owner
     *
     * @return the specification as a filter predicate
     */
    public Specification<UserAdmin> isContractOwner() {

        return new Specification<UserAdmin>() {
            @Override
            public Predicate toPredicate(Root<UserAdmin> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<UserAdminRole, UserAdminPermission> permissions = root.join(UserAdmin_.roles).join(UserAdminUserAdminRole_.userAdminRole).join(UserAdminRole_.permissions);
                return cb.equal(permissions.get(UserAdminPermission_.name), PERM_ADMIN_CONTRACT_OWNER);
            }
        };
    }
}
