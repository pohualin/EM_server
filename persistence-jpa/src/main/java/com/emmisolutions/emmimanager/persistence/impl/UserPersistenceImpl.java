package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.UserSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Repo to deal with user persistence.
 */
@Repository
public class UserPersistenceImpl implements UserPersistence {

    @Resource
    UserAdminRepository userAdminRepository;

    @Resource
    UserSpecifications userSpecifications;

    @Override
    public UserAdmin saveOrUpdate(UserAdmin user) {
        user.setLogin(StringUtils.lowerCase(user.getLogin()));
        return userAdminRepository.save(user);
    }

    @Override
    public UserAdmin reload(String login) {
        return userAdminRepository.findByLoginIgnoreCase(login);
    }

    @Override
    public UserAdmin fetchUserWillFullPermissions(String login) {
        return userAdminRepository.fetchWithFullPermissions(login);
    }


    @Override
    public Page<UserAdmin> listPotentialContractOwners(Pageable pageable) {
        if (pageable == null){
            // default pagination request if none
            pageable = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return userAdminRepository.findAll(where(userSpecifications.isContractOwner()), pageable);
    }
}
