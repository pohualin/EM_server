package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientTeamPhoneConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamPhoneConfigurationPersistence;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        
        DefaultClientTeamPhoneConfiguration defaultPhoneConfig =  defaultClientTeamPhoneConfigurationPersistence.find();
        
                   
        ClientTeamPhoneConfiguration phoneConfig = new ClientTeamPhoneConfiguration();
        phoneConfig.setTeam(team);
        phoneConfig.setCreatedBy(new UserAdmin(1l));
        phoneConfig.setCollectPhone(defaultPhoneConfig.isCollectPhone());
        phoneConfig.setRequirePhone(defaultPhoneConfig.isRequirePhone());
                
              
        ClientTeamPhoneConfiguration configurationSave = clientTeamPhoneConfigurationPersistence.save(phoneConfig);
               
        ClientTeamPhoneConfiguration listOfPhoneConfig = clientTeamPhoneConfigurationPersistence.
        		find(team.getId());
              

        assertThat("should contain configuration",
                listOfPhoneConfig, is(configurationSave));
        
               
        ClientTeamPhoneConfiguration configurationReload = clientTeamPhoneConfigurationPersistence.reload(configurationSave.getId());
        
        assertThat("should reload the same configuration",
                configurationSave, is(configurationReload));
     
    
    }
    
}
