package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.User;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserService;
import org.junit.Test;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * An integration test that goes across a wired persistence layer as well
 */
public class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserService userService;

    @Test(expected = ConstraintViolationException.class)
    public void testUserCreateWithoutLogin() {
        userService.save(new User());
    }

    public void testUserCreate() {
        User user = new User("login", "pw");
        user = userService.save(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
    }
}
