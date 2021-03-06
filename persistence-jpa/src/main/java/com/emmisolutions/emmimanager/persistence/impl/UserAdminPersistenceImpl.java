package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.UserAdminSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminUserAdminRole;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.MatchingCriteriaBean;
import com.emmisolutions.emmimanager.persistence.impl.specification.UserSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminRoleRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminUserAdminRoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Repo to deal with user persistence.
 */
@Repository
public class UserAdminPersistenceImpl implements UserAdminPersistence {

    @Resource
    UserAdminRoleRepository userAdminroleRepository;

    @Resource
    UserAdminRepository userAdminRepository;

    @Resource
    UserAdminUserAdminRoleRepository userAdminUserAdminRoleRepository;

    @Resource
    UserSpecifications userSpecifications;

    @Resource
    MatchingCriteriaBean matchCriteria;

    @Override
    public Set<UserAdminUserAdminRole> saveAll(
            List<UserAdminUserAdminRole> userAdminUserAdminRole) {
        return new HashSet<>(
                userAdminUserAdminRoleRepository.save(userAdminUserAdminRole));
    }

    @Override
    public UserAdmin saveOrUpdate(UserAdmin user) {
        user.setLogin(StringUtils.lowerCase(user.getLogin()));
        user.setEmail(StringUtils.lowerCase(user.getEmail()));
        user.setNormalizedName(matchCriteria.normalizedName(
                user.getFirstName(), user.getLastName(), user.getLogin(),
                user.getEmail()));
        return userAdminRepository.save(user);
    }

    @Override
    public long removeAllAdminRoleByUserAdmin(UserAdmin user) {
        long ret = userAdminUserAdminRoleRepository.deleteAllByUserAdmin(user);
        userAdminUserAdminRoleRepository.flush();
        return ret;
    }

    @Override
    public UserAdmin reload(String login) {
        if (StringUtils.isBlank(login)){
            return null;
        }
        return userAdminRepository.fetchWithFullPermissions(StringUtils.lowerCase(login));
    }

    @Override
    public UserAdmin reload(UserAdmin user) {
        if (user == null || user.getId() == null) {
            return null;
        }
        return userAdminRepository.findOne(user.getId());
    }

    @Override
    public UserAdmin fetchUserWillFullPermissions(String login) {
        return reload(login);
    }

    @Override
    public Page<UserAdmin> listPotentialContractOwners(Pageable pageable) {
        if (pageable == null) {
            // default pagination request if none
            pageable = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }

        return userAdminRepository.findAll(
                where(userSpecifications.isContractOwner()).and(
                        userSpecifications.isNotSystemUser()), pageable);
    }

    @Override
    public Page<UserAdmin> list(Pageable pageable, UserAdminSearchFilter filter) {
        if (pageable == null) {
            pageable = new PageRequest(0, 10, Sort.Direction.ASC, "id");
        }
        return userAdminRepository.findAll(
                where(userSpecifications.hasNames(filter)).and(
                        userSpecifications.isNotSystemUser()), pageable);
    }

    @Override
    public Page<UserAdminRole> listRolesWithoutSystem(Pageable pageable) {
        if (pageable == null) {
            // default pagination request if none
            pageable = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return userAdminroleRepository.findAllWithoutSystem(pageable);
    }

    @Override
    public Set<UserAdmin> findConflictingUsers(UserAdmin userAdmin) {
        if (userAdmin == null || StringUtils.isBlank(userAdmin.getEmail())) {
            // nothing to search on
            return Collections.emptySet();
        }
        return new HashSet<>(userAdminRepository.findAll(where(
                userSpecifications.hasEmail(userAdmin)).and(
                userSpecifications.isNotSelf(userAdmin))));
    }

    @Override
    public boolean isSystemUser(UserAdmin userAdmin) {
        boolean isSystemUser = false;
        if (userAdmin != null) {
            UserAdmin toCheck;
            UserAdmin fromDb = reload(userAdmin);
            if (fromDb != null) {
                // an existing user
                toCheck = fromDb;
            } else {
                // non-persistent user
                toCheck = userAdmin;
            }
            // check for the presence of a system role
            if (!CollectionUtils.isEmpty(toCheck.getRoles())) {
                for (UserAdminUserAdminRole userAdminUserAdminRole : toCheck.getRoles()) {
                    UserAdminRole adminRole = userAdminUserAdminRole.getUserAdminRole();
                    if (adminRole.getId() != null) {
                        UserAdminRole reloaded = userAdminroleRepository.findOne(adminRole.getId());
                        if (reloaded != null && reloaded.isSystemRole()) {
                            isSystemUser = true;
                            break;
                        }
                    }
                }
            }
        }
        return isSystemUser;
    }
}
