package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.Permission;
import com.emmisolutions.emmimanager.model.Role;
import com.emmisolutions.emmimanager.model.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

import static com.emmisolutions.emmimanager.model.PermissionName.PERM_CONTRACT_OWNER;

/**
 * Specifications for a User.
 */
public class UserSpecifications {

    private UserSpecifications(){}

    /**
     * Filter to ensure User is a contract owner
     *
     * @return he specification as a filter predicate
     */
    public static Specification<User> isContractOwner() {

        return new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join<Role, Permission> permissions = root.join(User_.roles).join(Role_.permissions);
                return cb.equal(permissions.get(Permission_.name), PERM_CONTRACT_OWNER);
            }
        };
    }
}
