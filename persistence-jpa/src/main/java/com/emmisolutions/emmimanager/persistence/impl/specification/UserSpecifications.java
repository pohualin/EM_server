package com.emmisolutions.emmimanager.persistence.impl.specification;

import static com.emmisolutions.emmimanager.model.PermissionName.PERM_CONTRACT_OWNER;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.emmisolutions.emmimanager.model.Permission;
import com.emmisolutions.emmimanager.model.Permission_;
import com.emmisolutions.emmimanager.model.Role;
import com.emmisolutions.emmimanager.model.Role_;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.model.User_;

/**
 * Specifications for a User.
 */
public class UserSpecifications {

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
