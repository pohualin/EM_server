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
import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientTeamSchedulingConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;

/**
 * An integration test for ClientTeamSchedulingConfigurationServiceImpl
 */
public class ClientTeamSchedulingConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientTeamSchedulingConfigurationService clientTeamSchedulingConfigurationService;
    
    @Resource
    TeamService teamService;
    
    /**
     * Test CRUD
     */
    @Test
    public void testCreateReloadUpdate() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        
        ClientTeamSchedulingConfiguration SchedulingConfig = new ClientTeamSchedulingConfiguration();
        SchedulingConfig.setTeam(team);
        SchedulingConfig.setCreatedBy("system");
        SchedulingConfig.setUseProvider(true);
        SchedulingConfig.setUseLocation(true);
                
              
        ClientTeamSchedulingConfiguration SchedulingConfigSaved= clientTeamSchedulingConfigurationService.saveOrUpdate(SchedulingConfig);
                   
        ClientTeamSchedulingConfiguration listOfSchedulingConfig  = clientTeamSchedulingConfigurationService.findByTeam(team);
        
        assertThat("should contain Scheduling configuration",
        		listOfSchedulingConfig, is(SchedulingConfigSaved));
        
           				
          
    }
    
    /**
     * Test CRUD
     */
    @Test
    public void testFind() {
    	Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
                
        ClientTeamSchedulingConfiguration listOfSchedulingConfig  = clientTeamSchedulingConfigurationService.findByTeam(team);
        
        if(listOfSchedulingConfig != null){
        	Team reloadTeam = teamService.reload(team);
        
            ClientTeamSchedulingConfiguration SchedulingConfig = new ClientTeamSchedulingConfiguration();
            SchedulingConfig.setTeam(reloadTeam);
            SchedulingConfig.setCreatedBy("system");
            SchedulingConfig.setUseProvider(true);
            SchedulingConfig.setUseLocation(true);
       
            clientTeamSchedulingConfigurationService.saveOrUpdate(SchedulingConfig);
            ClientTeamSchedulingConfiguration SchedulingConfigSaved = clientTeamSchedulingConfigurationService.findByTeam(reloadTeam);
            assertThat("should contain Scheduling configuration",
            		SchedulingConfig, is(SchedulingConfigSaved));
            
  				
         
        }
 
    }
    
    /**
     * Test bad saveAndUpdate
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeSaveOrUpdateNull() {
    	clientTeamSchedulingConfigurationService.saveOrUpdate(null);
    }
    
    
    /**
     * Test bad saveAndUpdate
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeFindNull() {
    	clientTeamSchedulingConfigurationService.findByTeam(new Team());
    }
    
    
}
