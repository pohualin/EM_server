package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
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

    /**
     * Create without login
     */
    @Test(expected = ConstraintViolationException.class)
    public void testUserCreateWithoutLogin() {
        userService.save(new UserAdmin());
    }

    /**
     * Create with required values
     */
    @Test
    public void testUserCreate() {
        UserAdmin user = new UserAdmin("login", "pw");
        user = userService.save(user);
        assertThat(user.getId(), is(notNullValue()));
        assertThat(user.getVersion(), is(notNullValue()));
    }

}
