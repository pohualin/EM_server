package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientTeamSchedulingConfigurationPersistence;

/**
 * Integration test for ClientTeamSchedulingConfigurationPersistence
 */
public class ClientTeamSchedulingConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientTeamSchedulingConfigurationPersistence clientTeamSchedulingConfigurationPersistence;
    
    /**
     * Test list
     */
    @Test
    public void testList() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
                          
        ClientTeamSchedulingConfiguration SchedulingConfig = new ClientTeamSchedulingConfiguration();
        SchedulingConfig.setTeam(team);
        SchedulingConfig.setCreatedBy("system");
        SchedulingConfig.setUseProvider(true);
        SchedulingConfig.setUseLocation(true);
                
              
        ClientTeamSchedulingConfiguration configurationSave = clientTeamSchedulingConfigurationPersistence.save(SchedulingConfig);
               
        ClientTeamSchedulingConfiguration listOfSchedulingConfig = clientTeamSchedulingConfigurationPersistence.
        		find(team.getId());
              

        assertThat("should contain configuration",
                listOfSchedulingConfig, is(configurationSave));
        
               
        ClientTeamSchedulingConfiguration configurationReload = clientTeamSchedulingConfigurationPersistence.reload(configurationSave.getId());
        
        assertThat("should reload the same configuration",
                configurationSave, is(configurationReload));
     
    
    }
    
}
