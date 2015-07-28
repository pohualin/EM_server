package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientTeamSchedulingConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamSchedulingConfigurationPersistence;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Integration test for ClientTeamSchedulingConfigurationPersistence
 */
public class ClientTeamSchedulingConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientTeamSchedulingConfigurationPersistence clientTeamSchedulingConfigurationPersistence;

    @Resource
    DefaultClientTeamSchedulingConfigurationPersistence defaultClientTeamSchedulingConfigurationPersistence;

    /**
     * Test list
     */
    @Test
    public void testList() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);

        DefaultClientTeamSchedulingConfiguration defaultConfig = defaultClientTeamSchedulingConfigurationPersistence
                .findActive();

        ClientTeamSchedulingConfiguration SchedulingConfig = new ClientTeamSchedulingConfiguration();
        SchedulingConfig.setTeam(team);
        SchedulingConfig.setDefaultClientTeamSchedulingConfiguration(defaultConfig);
        SchedulingConfig.setCreatedBy(new UserAdmin(1l));
        SchedulingConfig.setUseProvider(true);
        SchedulingConfig.setUseLocation(true);

        ClientTeamSchedulingConfiguration configurationSave = clientTeamSchedulingConfigurationPersistence
                .save(SchedulingConfig);

        ClientTeamSchedulingConfiguration listOfSchedulingConfig = clientTeamSchedulingConfigurationPersistence
                .find(team.getId());

        assertThat("should contain configuration", listOfSchedulingConfig,
                is(configurationSave));

        ClientTeamSchedulingConfiguration configurationReload = clientTeamSchedulingConfigurationPersistence
                .reload(configurationSave.getId());

        assertThat("should reload the same configuration", configurationSave,
                is(configurationReload));

    }

}
