package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRoleType;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserClientReferenceRolePersistence;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test Client Reference Role persistence
 */
public class UserClientReferenceRolePersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientReferenceRolePersistence userClientReferenceRolePersistence;

    @Test
    public void load() {
        assertThat("Reference Roles are loaded", userClientReferenceRolePersistence.loadReferenceRoles(new PageRequest(0,1)).getTotalElements(), is(not(0l)));
    }

    @Test
    public void reload(){
        assertThat("reload works", userClientReferenceRolePersistence.reload(new UserClientReferenceRoleType(1l)), is(notNullValue()));
    }
}
