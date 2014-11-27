package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRoleType;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.UserClientReferenceTeamRolePersistence;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test team Reference Role persistence
 */
public class UserClientReferenceTeamRolePersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    UserClientReferenceTeamRolePersistence userClientReferenceTeamRolePersistence;

    @Test
    public void load() {
        assertThat("Reference Roles are loaded", userClientReferenceTeamRolePersistence.loadReferenceTeamRoles(new PageRequest(0, 1)).getTotalElements(), is(not(0l)));
    }

    @Test
    public void reload() {
        assertThat("reload works", userClientReferenceTeamRolePersistence.reload(new UserClientReferenceTeamRoleType(1l)), is(notNullValue()));
    }
}
