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
        
        DefaultClientTeamPhoneConfiguration defaultPhoneConfigList =  defaultClientTeamPhoneConfigurationPersistence.find();
        
        DefaultClientTeamPhoneConfiguration defaultPhoneConfig = defaultPhoneConfigList;
        ClientTeamPhoneConfiguration phoneConfig = new ClientTeamPhoneConfiguration();
        phoneConfig.setTeam(team);
        phoneConfig.setCreatedBy("system");
        phoneConfig.setCollectPhone(defaultPhoneConfig.isCollectPhone());
        phoneConfig.setRequirePhone(defaultPhoneConfig.isRequirePhone());
                
              
        ClientTeamPhoneConfiguration phoneConfigSaved= clientTeamPhoneConfigurationService.saveOrUpdate(phoneConfig);
                   
        ClientTeamPhoneConfiguration listOfPhoneConfig  = clientTeamPhoneConfigurationService.findByTeam(team);
        
        assertThat("should contain phone configuration",
        		listOfPhoneConfig, is(phoneConfigSaved));
        
           				
          
    }
    
    /**
     * Test CRUD
     */
    @Test
    public void testFind() {
    	Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
                
        ClientTeamPhoneConfiguration listOfPhoneConfig  = clientTeamPhoneConfigurationService.findByTeam(team);
        
        if(listOfPhoneConfig != null){
        	Team reloadTeam = teamService.reload(team);
            List<ClientTeamPhoneConfiguration> list = new ArrayList<ClientTeamPhoneConfiguration> ();
            
            DefaultClientTeamPhoneConfiguration defaultPhoneConfigList =  defaultClientTeamPhoneConfigurationPersistence.find();
            
            DefaultClientTeamPhoneConfiguration defaultPhoneConfig = defaultPhoneConfigList;
           
            ClientTeamPhoneConfiguration phoneConfig = new ClientTeamPhoneConfiguration();
            phoneConfig.setTeam(reloadTeam);
            phoneConfig.setCreatedBy("system");
            phoneConfig.setCollectPhone(defaultPhoneConfig.isCollectPhone());
            phoneConfig.setRequirePhone(defaultPhoneConfig.isRequirePhone());
               
           
            ClientTeamPhoneConfiguration phoneConfigSaved= clientTeamPhoneConfigurationService.saveOrUpdate(phoneConfig);
                        
            assertThat("should contain Phone configuration",
            		listOfPhoneConfig, is(phoneConfigSaved));
            
  				
         
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
    	clientTeamPhoneConfigurationService.findByTeam(new Team());
    }
    
    
}
