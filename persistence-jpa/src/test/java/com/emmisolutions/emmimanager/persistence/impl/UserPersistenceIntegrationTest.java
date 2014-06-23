package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Integration test sample.
 */
public class UserPersistenceIntegrationTest extends BaseIntegrationTest {

    final static Logger logger = LoggerFactory.getLogger(UserPersistenceIntegrationTest.class);

    @Resource
    UserPersistence userPersistence;

    @Resource
    UserRepository userRepository;

    @Test
    public void testSetup() {
        logger.debug("Inside the test setup");
        User user = new User();
        user = userPersistence.saveOrUpdate(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        User user1 = userRepository.findOne(user.getId());
        assertThat(user, is(user1));

    }
}
