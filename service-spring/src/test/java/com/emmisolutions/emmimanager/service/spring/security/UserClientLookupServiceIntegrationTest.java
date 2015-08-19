package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.security.UserClientLookupService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests the user client lookup service
 */
public class UserClientLookupServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientLookupService userClientLookupService;

    /**
     * Not really much to test here as the service methods are simple finders
     * with nothing but a delegation to the persistence layer which is tested
     * thoroughly elsewhere.
     */
    @Test
    public void nothingReallyToTest() {
        assertThat("can call findByActivationKey",
                userClientLookupService.findByActivationKey(null), is(nullValue()));
        assertThat("can call findByResetToken",
                userClientLookupService.findByResetToken(null), is(nullValue()));
        assertThat("can call findByValidationToken",
                userClientLookupService.findByValidationToken(null), is(nullValue()));
    }

}
