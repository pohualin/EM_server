package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.persistence.impl.specification.UserSpecifications.isContractOwner;
import static org.springframework.data.jpa.domain.Specifications.where;

@Repository
public class UserPersistenceImpl implements UserPersistence {

    @Resource
    UserRepository userRepository;

    @Override
    public User saveOrUpdate(User user) {
        user.setLogin(StringUtils.lowerCase(user.getLogin()));
        return userRepository.save(user);
    }

    @Override
    public User reload(String login) {
        return userRepository.findByLoginIgnoreCase(login);
    }

    @Override
    public User fetchUserWillFullPermissions(String login) {
        return userRepository.fetchWithFullPermissions(login);
    }


    @Override
    public Page<User> listPotentialContractOwners(Pageable pageable) {
        if (pageable == null){
            // default pagination request if none
            pageable = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return userRepository.findAll(where(isContractOwner()), pageable);
    }
}
