package com.emmisolutions.emmimanager.service.spring.audit;

import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.model.audit.login.LoginStatusName.SUCCESS;
import static com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService.APPLICATION.ADMIN_FACING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Test the audit service
 */
public class AuditServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    AuthenticationAuditService authenticationAuditService;

    @Test(expected = DataIntegrityViolationException.class)
    public void loginIncomplete() {
        authenticationAuditService.login(null, null, null, null, null);
    }

    @Test
    public void login() {
        assertThat("should be good to go",
                authenticationAuditService.login(null, null, null, SUCCESS, ADMIN_FACING),
                is(notNullValue()));
    }
}
