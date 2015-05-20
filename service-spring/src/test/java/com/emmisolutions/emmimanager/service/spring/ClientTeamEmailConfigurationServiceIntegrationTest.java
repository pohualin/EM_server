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
import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamEmailConfigurationPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.ClientTeamEmailConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;

/**
 * An integration test for ClientTeamEmailConfigurationServiceImpl
 */
public class ClientTeamEmailConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientTeamEmailConfigurationService clientTeamEmailConfigurationService;
    
    @Resource
    DefaultClientTeamEmailConfigurationPersistence defaultClientTeamEmailConfigurationPersistence;
    
    @Resource
    TeamService teamService;
    
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
        emailConfig.setDescription(defaultEmailConfig.getDescription());
        emailConfig.setTeam(team);
        emailConfig.setCreatedBy("system");
        emailConfig.setRank(defaultEmailConfig.getRank());
        emailConfig.setType(defaultEmailConfig.getType());
        emailConfig.setDescription(defaultEmailConfig.getDescription());
        emailConfig.setEmail_config(defaultEmailConfig.isDefaultValue());
        
        ClientTeamEmailConfiguration emailConfigTwo = new ClientTeamEmailConfiguration();
        emailConfigTwo.setDescription(defaultEmailConfigTwo.getDescription());
        emailConfigTwo.setTeam(team);
        emailConfigTwo.setCreatedBy("system");
        emailConfigTwo.setRank(defaultEmailConfigTwo.getRank());
        emailConfigTwo.setType(defaultEmailConfigTwo.getType());
        emailConfigTwo.setDescription(defaultEmailConfigTwo.getDescription());
        emailConfigTwo.setEmail_config(defaultEmailConfigTwo.isDefaultValue());
        
        List<ClientTeamEmailConfiguration> emailConfigList = new ArrayList<ClientTeamEmailConfiguration>();
        emailConfigList.add(emailConfig);
        emailConfigList.add(emailConfigTwo);
        
        ClientTeamEmailConfiguration emailConfigSaved= clientTeamEmailConfigurationService.saveOrUpdate(emailConfig);
        ClientTeamEmailConfiguration emailConfigTwoSaved= clientTeamEmailConfigurationService.saveOrUpdate(emailConfigTwo);
             
             
        Page<ClientTeamEmailConfiguration> listOfEmailConfig  = clientTeamEmailConfigurationService.findByTeam(team, null);
        
        ClientTeamEmailConfiguration reloadEmailConfig = clientTeamEmailConfigurationService.reload(listOfEmailConfig.getContent().get(0).getId());
        
        
        assertThat("should contain email configuration",
        		listOfEmailConfig.getContent(), hasItem(emailConfigSaved));
        
        assertThat("should contain eamil configuration",
        		listOfEmailConfig.getContent(), hasItem(emailConfigTwoSaved));
        				
        assertThat("reload the same instance email configuration",
        		reloadEmailConfig.getId(), is(emailConfigSaved.getId()));
        
        
          
    }
    
    /**
     * Test CRUD
     */
    @Test
    public void testFind() {
    	Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
                
        Page<ClientTeamEmailConfiguration> listOfEmailConfig  = clientTeamEmailConfigurationService.findByTeam(team, null);
        
        assertThat("should not contain email configuration",
        		listOfEmailConfig.getContent().size() < 1, is(false));
        
        if(!listOfEmailConfig.hasContent()){
        	Team reloadTeam = teamService.reload(team);
            List<ClientTeamEmailConfiguration> list = new ArrayList<ClientTeamEmailConfiguration> ();
            
            Page<DefaultClientTeamEmailConfiguration> defaultEmailConfigList =  defaultClientTeamEmailConfigurationPersistence.findActive(new PageRequest(0, 10));
            
            DefaultClientTeamEmailConfiguration defaultEmailConfig = defaultEmailConfigList.getContent().get(0);
            DefaultClientTeamEmailConfiguration defaultEmailConfigTwo = defaultEmailConfigList.getContent().get(1);

            ClientTeamEmailConfiguration emailConfig = new ClientTeamEmailConfiguration();
            emailConfig.setDescription(defaultEmailConfig.getDescription());
            emailConfig.setTeam(reloadTeam);
            emailConfig.setCreatedBy("system");
            emailConfig.setRank(defaultEmailConfig.getRank());
            emailConfig.setType(defaultEmailConfig.getType());
            emailConfig.setEmail_config(defaultEmailConfig.isDefaultValue());
            
            ClientTeamEmailConfiguration emailConfigTwo = new ClientTeamEmailConfiguration();
            emailConfigTwo.setDescription(defaultEmailConfigTwo.getDescription());
            emailConfigTwo.setTeam(reloadTeam);
            emailConfigTwo.setCreatedBy("system");
            emailConfigTwo.setRank(defaultEmailConfigTwo.getRank());
            emailConfigTwo.setType(defaultEmailConfigTwo.getType());
            emailConfigTwo.setEmail_config(defaultEmailConfigTwo.isDefaultValue());
            
            List<ClientTeamEmailConfiguration> emailConfigList = new ArrayList<ClientTeamEmailConfiguration>();
            emailConfigList.add(emailConfig);
            emailConfigList.add(emailConfigTwo);
            
            ClientTeamEmailConfiguration emailConfigSaved= clientTeamEmailConfigurationService.saveOrUpdate(emailConfig);
            ClientTeamEmailConfiguration emailConfigTwoSaved= clientTeamEmailConfigurationService.saveOrUpdate(emailConfigTwo);
            
            ClientTeamEmailConfiguration reloadEmailConfig = clientTeamEmailConfigurationService.reload(listOfEmailConfig.getContent().get(0).getId());
  
            assertThat("should contain email configuration",
            		listOfEmailConfig.getContent(), hasItem(emailConfigSaved));
            
            assertThat("should contain eamil configuration",
            		listOfEmailConfig.getContent(), hasItem(emailConfigTwoSaved));
            				
            assertThat("reload the same instance email configuration",
            		emailConfigSaved, is(reloadEmailConfig));
            
        }
 
    }
    
    
    /**
     * Test bad Reload
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeReloadNullId() {
    	clientTeamEmailConfigurationService
                .reload(null);
    }
    
    /**
     * Test bad saveAndUpdate
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeSaveOrUpdateNull() {
    	clientTeamEmailConfigurationService.saveOrUpdate(null);
    }
    
    
    /**
     * Test bad saveAndUpdate
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeFindNull() {
    	clientTeamEmailConfigurationService.findByTeam(new Team(), null);
    }
    
    
}
