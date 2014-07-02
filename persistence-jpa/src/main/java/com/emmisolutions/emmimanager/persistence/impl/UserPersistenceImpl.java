package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
public class UserPersistenceImpl implements UserPersistence {

    @PersistenceContext
    protected EntityManager entityManager;

    @Resource
    UserRepository userRepository;

    @Override
    public User saveOrUpdate(User user) {
        if (user.getId() == null) {
            user.setCreatedBy("system");
            user.setLastModifiedBy("system");
        }
        // delegate to spring-data repo for simple kinds of things
        return userRepository.save(user);
    }
}
