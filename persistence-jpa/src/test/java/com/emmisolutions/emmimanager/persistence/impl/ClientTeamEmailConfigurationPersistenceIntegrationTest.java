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


        ClientTeamEmailConfiguration configuration = new ClientTeamEmailConfiguration();
        configuration.setTeamEmailConfigurationId(defaultEmailConfig.getId());
        configuration.setDescription(defaultEmailConfig.getDescription());
        configuration.setClient(client);
        configuration.setTeam(team);
        
        ClientTeamEmailConfiguration configurationTwo = new ClientTeamEmailConfiguration();
        configurationTwo.setTeamEmailConfigurationId(defaultEmailConfigTwo.getId());
        configurationTwo.setDescription(defaultEmailConfigTwo.getDescription());
        configurationTwo.setClient(client);
        configurationTwo.setTeam(team);
        
        
        ClientTeamEmailConfiguration configurationSave = clientTeamEmailConfigurationPersistence.save(configuration);
        ClientTeamEmailConfiguration configurationSaveTwo = clientTeamEmailConfigurationPersistence.save(configurationTwo);
        
        Page<ClientTeamEmailConfiguration> listOfEmailConfig = clientTeamEmailConfigurationPersistence.
        		find(client.getId(), team.getId(), null);
              

        assertThat("should contain configuration",
                listOfEmailConfig.getContent(), hasItem(configurationSave));
        
        assertThat("should contain configuration",
                listOfEmailConfig.getContent(), hasItem(configurationSaveTwo));
        
        ClientTeamEmailConfiguration configurationReload = clientTeamEmailConfigurationPersistence.reload(configurationSave.getId());
        
        assertThat("should reload the same configuration",
                configurationSave.getId() == configurationReload.getId(), is(true));
               
        
        // configuration.setDescription(RandomStringUtils.randomAlphabetic(255));
        //configuration.setEmailEnding("emmisolutions.com");
       /* configuration = emailRestrictConfigurationPersistence
                .saveOrUpdate(configuration);

        EmailRestrictConfiguration configurationA = new EmailRestrictConfiguration();
        configurationA.setClient(client);
        configurationA.setDescription(RandomStringUtils.randomAlphabetic(255));
        configurationA.setEmailEnding("emmisolutions.net");
        configurationA = emailRestrictConfigurationPersistence
                .saveOrUpdate(configuration);

        Page<EmailRestrictConfiguration> listOfEmailConfig = emailRestrictConfigurationPersistence
                .list(new PageRequest(0, 10), client.getId());

        assertThat("should contain configuration",
                listOfEmailConfig.getContent(), hasItem(configuration));
        assertThat("should contain configurationA",
                listOfEmailConfig.getContent(), hasItem(configurationA));

        Page<EmailRestrictConfiguration> listOfEmailConfigA = emailRestrictConfigurationPersistence
                .list(null, client.getId());

        assertThat("should contain configuration",
                listOfEmailConfigA.getContent(), hasItem(configuration));
        assertThat("should contain configurationA",
                listOfEmailConfigA.getContent(), hasItem(configurationA));

        Page<EmailRestrictConfiguration> listOfEmailConfigB = emailRestrictConfigurationPersistence
                .list(new PageRequest(0, 10), null);

        assertThat("should contain configuration",
                listOfEmailConfigB.getContent(), hasItem(configuration));
        assertThat("should contain configurationA",
                listOfEmailConfigB.getContent(), hasItem(configurationA));*/
    }
    
}
