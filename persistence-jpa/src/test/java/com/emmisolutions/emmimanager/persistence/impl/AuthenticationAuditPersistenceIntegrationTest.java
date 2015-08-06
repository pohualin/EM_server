package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.audit.login.Login;
import com.emmisolutions.emmimanager.model.audit.login.LoginStatus;
import com.emmisolutions.emmimanager.model.audit.logout.Logout;
import com.emmisolutions.emmimanager.model.audit.logout.LogoutSource;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.AuthenticationAuditPersistence;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.model.audit.login.LoginStatusName.EXPIRED;
import static com.emmisolutions.emmimanager.model.audit.login.LoginStatusName.SUCCESS;
import static com.emmisolutions.emmimanager.model.audit.logout.LogoutSourceName.USER;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Authentication persistence integration test
 */
public class AuthenticationAuditPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    AuthenticationAuditPersistence authenticationAuditPersistence;

    @Test
    public void login() {
        Login loginAttempt = new Login();
        loginAttempt.setIpAddress("12.24.33.145");
        loginAttempt.setLogin("login@login.com");
        loginAttempt.setUser(new UserAdmin(1l));
        loginAttempt.setStatus(new LoginStatus(SUCCESS));
        loginAttempt.setTime(DateTime.now(DateTimeZone.UTC));
        assertThat("save was successful", authenticationAuditPersistence.login(loginAttempt), is(notNullValue()));
    }

    @Test
    public void logout() {
        Logout logout = new Logout();
        logout.setTime(DateTime.now(DateTimeZone.UTC));
        logout.setUser(new UserAdmin(1l));
        logout.setSource(new LogoutSource(USER));
        assertThat("save was successful", authenticationAuditPersistence.logout(logout), is(notNullValue()));
    }

    @Test
    public void loginWithoutUser() {
        Login loginAttempt = new Login();
        loginAttempt.setIpAddress("12.24.33.145");
        loginAttempt.setLogin("login@login.com");
        loginAttempt.setUser(new UserAdmin()); // user without an id should be ignored
        loginAttempt.setStatus(new LoginStatus(EXPIRED));
        loginAttempt.setTime(DateTime.now(DateTimeZone.UTC));
        assertThat("save was successful", authenticationAuditPersistence.login(loginAttempt), is(notNullValue()));
    }

    @Test
    public void nulls() {
        assertThat("null doesn't fail", authenticationAuditPersistence.login(null), is(nullValue()));
        assertThat("null doesn't fail", authenticationAuditPersistence.logout(null), is(nullValue()));
    }

}
