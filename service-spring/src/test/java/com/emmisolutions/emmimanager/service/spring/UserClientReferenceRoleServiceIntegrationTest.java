package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.UserClientReferenceRoleService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for UserClientReferenceRoleService
 */
public class UserClientReferenceRoleServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientReferenceRoleService userClientReferenceRoleService;

    /**
     * Make sure we can load a page
     */
    @Test
    public void load() {
        assertThat("Reference Roles are loaded",
            userClientReferenceRoleService.loadUserClientReferenceRoles(null).getTotalElements(), is(not(0l)));
    }
}
