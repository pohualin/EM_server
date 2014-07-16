package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;
import org.junit.Test;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Integration test sample.
 */
public class UserPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserPersistence userPersistence;

    @Resource
    UserRepository userRepository;

    @Test(expected = ConstraintViolationException.class)
    public void testConstraints() {
        // user is invalid without a login
        userPersistence.saveOrUpdate(new User());
    }

    @Test
    public void testCreate() {
        User user = new User("login", "pw");
        user = userPersistence.saveOrUpdate(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        User user1 = userRepository.findOne(user.getId());
        assertThat(user, is(user1));
    }

    @Test
    public void testLoad(){
        String login = "aLogin";
        User user = new User(login, "pw");
        user = userPersistence.saveOrUpdate(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));

        user = userPersistence.reload(login);
        assertThat("user is found", user, is(notNullValue()));
        assertThat(user.getLogin(), is(login.toLowerCase()));

    }

}
