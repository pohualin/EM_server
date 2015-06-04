package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamPhoneConfigurationPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientTeamPhoneConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;

/**
 * An integration test for ClientTeamPhoneConfigurationServiceImpl
 */
public class ClientTeamPhoneConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientTeamPhoneConfigurationService clientTeamPhoneConfigurationService;
    
    @Resource
    DefaultClientTeamPhoneConfigurationPersistence defaultClientTeamPhoneConfigurationPersistence;
    
    @Resource
    TeamService teamService;
    
    /**
     * Test CRUD
     */
    @Test
    public void testCreateReloadUpdate() {
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
               
        List<ClientTeamPhoneConfiguration> phoneConfigList = new ArrayList<ClientTeamPhoneConfiguration>();
        phoneConfigList.add(phoneConfig);
        phoneConfigList.add(phoneConfigTwo);
        
        ClientTeamPhoneConfiguration phoneConfigSaved= clientTeamPhoneConfigurationService.saveOrUpdate(phoneConfig);
        ClientTeamPhoneConfiguration phoneConfigTwoSaved= clientTeamPhoneConfigurationService.saveOrUpdate(phoneConfigTwo);
             
             
        Page<ClientTeamPhoneConfiguration> listOfPhoneConfig  = clientTeamPhoneConfigurationService.findByTeam(team, null);
        
        assertThat("should contain phone configuration",
        		listOfPhoneConfig.getContent(), hasItem(phoneConfigSaved));
        
        assertThat("should contain phone configuration",
        		listOfPhoneConfig.getContent(), hasItem(phoneConfigTwoSaved));
        				
          
    }
    
    /**
     * Test CRUD
     */
    @Test
    public void testFind() {
    	Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
                
        Page<ClientTeamPhoneConfiguration> listOfPhoneConfig  = clientTeamPhoneConfigurationService.findByTeam(team, null);
        
        assertThat("should not contain phone configuration",
        		listOfPhoneConfig.getContent().size() < 1, is(false));
        
        if(!listOfPhoneConfig.hasContent()){
        	Team reloadTeam = teamService.reload(team);
            List<ClientTeamPhoneConfiguration> list = new ArrayList<ClientTeamPhoneConfiguration> ();
            
            Page<DefaultClientTeamPhoneConfiguration> defaultPhoneConfigList =  defaultClientTeamPhoneConfigurationPersistence.findActive(new PageRequest(0, 10));
            
            DefaultClientTeamPhoneConfiguration defaultPhoneConfig = defaultPhoneConfigList.getContent().get(0);
            DefaultClientTeamPhoneConfiguration defaultPhoneConfigTwo = defaultPhoneConfigList.getContent().get(1);

            ClientTeamPhoneConfiguration phoneConfig = new ClientTeamPhoneConfiguration();
            phoneConfig.setTeam(reloadTeam);
            phoneConfig.setCreatedBy("system");
            phoneConfig.setRank(defaultPhoneConfig.getRank());
            phoneConfig.setType(defaultPhoneConfig.getType());
            phoneConfig.setPhoneConfig(defaultPhoneConfig.isDefaultValue());
                        
            ClientTeamPhoneConfiguration phoneConfigTwo = new ClientTeamPhoneConfiguration();
            phoneConfigTwo.setTeam(reloadTeam);
            phoneConfigTwo.setCreatedBy("system");
            phoneConfigTwo.setRank(defaultPhoneConfigTwo.getRank());
            phoneConfigTwo.setType(defaultPhoneConfigTwo.getType());
            phoneConfigTwo.setPhoneConfig(defaultPhoneConfigTwo.isDefaultValue());
                        
            List<ClientTeamPhoneConfiguration> phoneConfigList = new ArrayList<ClientTeamPhoneConfiguration>();
            phoneConfigList.add(phoneConfig);
            phoneConfigList.add(phoneConfigTwo);
            
            ClientTeamPhoneConfiguration phoneConfigSaved= clientTeamPhoneConfigurationService.saveOrUpdate(phoneConfig);
            ClientTeamPhoneConfiguration phoneConfigTwoSaved= clientTeamPhoneConfigurationService.saveOrUpdate(phoneConfigTwo);
            
            assertThat("should contain Phone configuration",
            		listOfPhoneConfig.getContent(), hasItem(phoneConfigSaved));
            
            assertThat("should contain Phone configuration",
            		listOfPhoneConfig.getContent(), hasItem(phoneConfigTwoSaved));
        				
         
        }
 
    }
    
    /**
     * Test bad saveAndUpdate
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeSaveOrUpdateNull() {
    	clientTeamPhoneConfigurationService.saveOrUpdate(null);
    }
    
    
    /**
     * Test bad saveAndUpdate
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeFindNull() {
    	clientTeamPhoneConfigurationService.findByTeam(new Team(), null);
    }
    
    
}
