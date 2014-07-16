package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;
import org.apache.commons.lang3.StringUtils;
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
        user.setLogin(StringUtils.lowerCase(user.getLogin()));
        // delegate to spring-data repo for simple kinds of things
        return userRepository.save(user);
    }

    @Override
    public User reload(String login) {
        return userRepository.findByLoginIgnoreCase(login);
    }

    @Override
    public User fetchUserWillFullPermissions(String login) {
        return entityManager.createQuery("select u from User u left join fetch u.roles r left join fetch r.permissions p where login = :login", User.class)
                .setParameter("login", login).getSingleResult();

    }
}
