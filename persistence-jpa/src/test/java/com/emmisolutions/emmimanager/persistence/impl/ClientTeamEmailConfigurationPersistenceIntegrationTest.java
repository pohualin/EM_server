package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientRestrictConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.ClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.EmailRestrictConfigurationPersistence;

/**
 * Integration test for EmailRestrictConfigurationPersistence
 */
public class ClientTeamEmailConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientTeamEmailConfigurationPersistence clientTeamEmailConfigurationPersistence;
    
    @Resource
    DefaultClientTeamEmailConfigurationPersistence defaultClientTeamEmailConfigurationPersistence;

     /**
     * Test list
     */
    @Test
    public void testList() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        
        Page<DefaultClientTeamEmailConfiguration> defaultEmailConfigList =  defaultClientTeamEmailConfigurationPersistence.findActive(new PageRequest(0, 10));
        
        DefaultClientTeamEmailConfiguration defaultEmailConfig = defaultEmailConfigList.getContent().get(0);
        DefaultClientTeamEmailConfiguration defaultEmailConfigTwo = defaultEmailConfigList.getContent().get(1);
        
        ClientTeamEmailConfiguration emailConfig = new ClientTeamEmailConfiguration();
        emailConfig.setTeam(team);
        emailConfig.setCreatedBy("system");
        emailConfig.setRank(defaultEmailConfig.getRank());
        emailConfig.setType(defaultEmailConfig.getType());
        emailConfig.setEmailConfig(defaultEmailConfig.isDefaultValue());
        emailConfig.setDefaulEmailConfiguration(defaultEmailConfig);
        
        ClientTeamEmailConfiguration emailConfigTwo = new ClientTeamEmailConfiguration();
        emailConfigTwo.setTeam(team);
        emailConfigTwo.setCreatedBy("system");
        emailConfigTwo.setRank(defaultEmailConfigTwo.getRank());
        emailConfigTwo.setType(defaultEmailConfigTwo.getType());
        emailConfigTwo.setEmailConfig(defaultEmailConfigTwo.isDefaultValue());
        emailConfigTwo.setDefaulEmailConfiguration(defaultEmailConfigTwo);

        ClientTeamEmailConfiguration configurationSave = clientTeamEmailConfigurationPersistence.save(emailConfig);
        ClientTeamEmailConfiguration configurationSaveTwo = clientTeamEmailConfigurationPersistence.save(emailConfigTwo);
        
        Page<ClientTeamEmailConfiguration> listOfEmailConfig = clientTeamEmailConfigurationPersistence.
        		find(team.getId(), null);
              

        assertThat("should contain configuration",
                listOfEmailConfig.getContent(), hasItem(configurationSave));
        
        assertThat("should contain configuration",
                listOfEmailConfig.getContent(), hasItem(configurationSaveTwo));
        
        ClientTeamEmailConfiguration configurationReload = clientTeamEmailConfigurationPersistence.reload(configurationSave.getId());
        
        assertThat("should reload the same configuration",
                configurationSave, is(configurationReload));
     
    
    }
    
}
