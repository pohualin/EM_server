package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.Team;

import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientTeamPhoneConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamPhoneConfigurationPersistence;

/**
 * Integration test for PhoneRestrictConfigurationPersistence
 */
public class ClientTeamPhoneConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientTeamPhoneConfigurationPersistence clientTeamPhoneConfigurationPersistence;
    
    @Resource
    DefaultClientTeamPhoneConfigurationPersistence defaultClientTeamPhoneConfigurationPersistence;

     /**
     * Test list
     */
    @Test
    public void testList() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        
        Page<DefaultClientTeamPhoneConfiguration> defaultPhoneConfigList =  defaultClientTeamPhoneConfigurationPersistence.findActive(new PageRequest(0, 10));
        
        DefaultClientTeamPhoneConfiguration defaultPhoneConfig = defaultPhoneConfigList.getContent().get(0);
        DefaultClientTeamPhoneConfiguration defaultPhoneConfigTwo = defaultPhoneConfigList.getContent().get(1);
        
        ClientTeamPhoneConfiguration phoneConfig = new ClientTeamPhoneConfiguration();
        phoneConfig.setTeam(team);
        phoneConfig.setCreatedBy("system");
        phoneConfig.setRank(defaultPhoneConfig.getRank());
        phoneConfig.setType(defaultPhoneConfig.getType());
        phoneConfig.setPhoneConfig(defaultPhoneConfig.isDefaultValue());
                
        ClientTeamPhoneConfiguration phoneConfigTwo = new ClientTeamPhoneConfiguration();
        phoneConfigTwo.setTeam(team);
        phoneConfigTwo.setCreatedBy("system");
        phoneConfigTwo.setRank(defaultPhoneConfigTwo.getRank());
        phoneConfigTwo.setType(defaultPhoneConfigTwo.getType());
        phoneConfigTwo.setPhoneConfig(defaultPhoneConfigTwo.isDefaultValue());
        
        ClientTeamPhoneConfiguration configurationSave = clientTeamPhoneConfigurationPersistence.save(phoneConfig);
        ClientTeamPhoneConfiguration configurationSaveTwo = clientTeamPhoneConfigurationPersistence.save(phoneConfigTwo);
        
        Page<ClientTeamPhoneConfiguration> listOfPhoneConfig = clientTeamPhoneConfigurationPersistence.
        		find(team.getId(), null);
              

        assertThat("should contain configuration",
                listOfPhoneConfig.getContent(), hasItem(configurationSave));
        
        assertThat("should contain configuration",
                listOfPhoneConfig.getContent(), hasItem(configurationSaveTwo));
        
        ClientTeamPhoneConfiguration configurationReload = clientTeamPhoneConfigurationPersistence.reload(configurationSave.getId());
        
        assertThat("should reload the same configuration",
                configurationSave, is(configurationReload));
     
    
    }
    
}
