package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.UserAdminSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminUserAdminRole;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.MatchingCriteriaBean;
import com.emmisolutions.emmimanager.persistence.impl.specification.UserSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminRoleRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminUserAdminRoleRepository;

/**
 * Repo to deal with user persistence.
 */
@Repository
public class UserPersistenceImpl implements UserPersistence {

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
            Set<UserAdminUserAdminRole> userAdminUserAdminRole) {
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
        return userAdminRepository.findByLoginIgnoreCase(login);
    }

    @Override
    public UserAdmin reload(UserAdmin user) {
        return userAdminRepository.findOne(user.getId());
    }

    @Override
    public UserAdmin fetchUserWillFullPermissions(String login) {
        return userAdminRepository.fetchWithFullPermissions(login);
    }

    @Override
    public Page<UserAdmin> listPotentialContractOwners(Pageable pageable) {
        if (pageable == null) {
            // default pagination request if none
            pageable = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return userAdminRepository.findAll(
                where(userSpecifications.isContractOwner()), pageable);
    }

    @Override
    public Page<UserAdmin> list(Pageable pageable, UserAdminSearchFilter filter) {
        if (pageable == null) {
            pageable = new PageRequest(0, 10, Sort.Direction.ASC, "id");
        }
        return userAdminRepository.findAll(
                where(userSpecifications.hasNames(filter)), pageable);
    }

    @Override
    public Page<UserAdminRole> listRolesWithoutSystem(Pageable pageable) {
        if (pageable == null) {
            // default pagination request if none
            pageable = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return userAdminroleRepository.findAllWithoutSystem(pageable);
    }
}
