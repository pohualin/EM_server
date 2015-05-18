package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientTeamEmailConfigurationService;

/**
 * An integration test for ClientTeamEmailConfigurationServiceImpl
 */
public class ClientTeamEmailConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientTeamEmailConfigurationService clientTeamEmailConfigurationService;
    
    @Resource
    DefaultClientTeamEmailConfigurationPersistence defaultClientTeamEmailConfigurationPersistence;

    /**
     * Test CRUD
     */
    @Test
    public void testCreateReloadUpdate() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        
        Page<DefaultClientTeamEmailConfiguration> defaultEmailConfigList =  defaultClientTeamEmailConfigurationPersistence.findActive(new PageRequest(0, 10));
        
        DefaultClientTeamEmailConfiguration defaultEmailConfig = defaultEmailConfigList.getContent().get(0);
        DefaultClientTeamEmailConfiguration defaultEmailConfigTwo = defaultEmailConfigList.getContent().get(1);

        ClientTeamEmailConfiguration emailConfig = new ClientTeamEmailConfiguration();
        emailConfig.setTeamEmailConfigurationId(defaultEmailConfig.getId());
        emailConfig.setDescription(defaultEmailConfig.getDescription());
        emailConfig.setClient(client);
        emailConfig.setTeam(team);
        
        ClientTeamEmailConfiguration emailConfigTwo = new ClientTeamEmailConfiguration();
        emailConfigTwo.setTeamEmailConfigurationId(defaultEmailConfigTwo.getId());
        emailConfigTwo.setDescription(defaultEmailConfigTwo.getDescription());
        emailConfigTwo.setClient(client);
        emailConfigTwo.setTeam(team);
        
        List<ClientTeamEmailConfiguration> emailConfigList = new ArrayList<ClientTeamEmailConfiguration>();
        emailConfigList.add(emailConfig);
        emailConfigList.add(emailConfigTwo);
        
        ClientTeamEmailConfiguration emailConfigSaved= clientTeamEmailConfigurationService.saveOrUpdate(emailConfig);
        ClientTeamEmailConfiguration emailConfigTwoSaved= clientTeamEmailConfigurationService.saveOrUpdate(emailConfigTwo);
             
             
        Page<ClientTeamEmailConfiguration> listOfEmailConfig  = clientTeamEmailConfigurationService.findByClientIdAndTeamId(client.getId(), team.getId(), null);
        
        ClientTeamEmailConfiguration reloadEmailConfig = clientTeamEmailConfigurationService.reload(listOfEmailConfig.getContent().get(0).getId());
        
        
        assertThat("should contain email configuration",
        		listOfEmailConfig.getContent(), hasItem(emailConfigSaved));
        
        assertThat("should contain eamil configuration",
        		listOfEmailConfig.getContent(), hasItem(emailConfigTwoSaved));
        				
        assertThat("reload the same instance email configuration",
        		reloadEmailConfig.getId(), is(emailConfigSaved.getId()));
        
        
          
    }
    
    /**
     * Test bad Reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeReloadNullId() {
    	clientTeamEmailConfigurationService
                .reload(null);
    }
    
}
