package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * An example of a Service class. It can only contact the persistence layer
 * and is responsible for Transaction demarcation. This layer will also have
 * security annotations at the method level as well.
 */
@Service
public class UserServiceImpl implements UserService {

    final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    UserPersistence userPersistence;

    @Override
    @Transactional
    public User save(User user) {
        logger.debug("About to save/update: {}", user);
        User ret = userPersistence.saveOrUpdate(user);
        logger.debug("Saved: {}", ret);
        return ret;
    }
}
