package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Repository
public class UserPersistenceImpl implements UserPersistence {

    final static Logger logger = LoggerFactory.getLogger(UserPersistenceImpl.class);

    @PersistenceContext(unitName = "EmmiManagerPersistenceUnit")
    protected EntityManager entityManager;

    @Resource
    UserRepository userRepository;

    @Override
    public User saveOrUpdate(User user) {
        logger.debug("About to save or update: {}", user);
        // delegate to spring-data repo for simple kinds of things
        entityManager.flush();
        User ret = userRepository.save(user);
        logger.debug("After update: {}", ret);
        return ret;
    }
}
