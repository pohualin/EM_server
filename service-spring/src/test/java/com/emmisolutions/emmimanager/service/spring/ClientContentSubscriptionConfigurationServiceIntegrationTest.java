package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientNote;
import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.InfoHeaderConfig;
import com.emmisolutions.emmimanager.model.PatientIdLabelConfig;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.ContentSubscriptionPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamSchedulingConfigurationPersistence;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientContentSubscriptionConfigurationService;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.ClientTeamSchedulingConfigurationService;
import com.emmisolutions.emmimanager.service.TeamService;

/**
 * An integration test for ClientContentSubscriptionConfigurationServiceImpl
 */
public class ClientContentSubscriptionConfigurationServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientContentSubscriptionConfigurationService clientContentSubscriptionConfigurationService;

    @Resource
    ClientService clientService;

    @Resource
    ContentSubscriptionPersistence contentSubscriptionPersistence;

    /**
     * Test CRUD
     */
    @Test
    public void testFindAndSave() {
        Client client = makeNewRandomClient();
                
        Page<ContentSubscription> contentSubscription= contentSubscriptionPersistence.findActive(null);
        ClientContentSubscriptionConfiguration clientContentSubscription = new ClientContentSubscriptionConfiguration();
        clientContentSubscription.setClient(client);
        clientContentSubscription.setContentSubscription(contentSubscription.getContent().get(0));
        clientContentSubscription.setFaithBased(false);
                
        ClientContentSubscriptionConfiguration savedContentSubscriptionConfig = clientContentSubscriptionConfigurationService.create(clientContentSubscription);
        
        assertThat(
                "should contain content subscription configuration faith based is false",
                savedContentSubscriptionConfig.isFaithBased()
                        , is(false));
            
        Page<ClientContentSubscriptionConfiguration> contentSubscriptionConfig = clientContentSubscriptionConfigurationService
                .findByClient(client, null);
        
        assertThat(
                "should has content subscription ",
                contentSubscriptionConfig.hasContent()
                        , is(true));
        
        assertThat("should has item as savedContentSubscriptionConfig",
        		contentSubscriptionConfig.getContent(), hasItem(savedContentSubscriptionConfig));
        
       
       clientContentSubscription.setFaithBased(true);
       
       ClientContentSubscriptionConfiguration updatedClientContentSubscription = clientContentSubscriptionConfigurationService.update(clientContentSubscription);
       
       assertThat(
               "should contain content subscription configuration faith based true after updated",
               updatedClientContentSubscription.isFaithBased()
                       , is(true));
       
       clientContentSubscriptionConfigurationService.delete(updatedClientContentSubscription);
       
       assertThat("delete content subscription successfully",
    		   clientContentSubscriptionConfigurationService.reload(updatedClientContentSubscription), is(nullValue()));
       
       assertThat("reload will return null with null object",
    		   clientContentSubscriptionConfigurationService.reload(new ClientContentSubscriptionConfiguration()), is(nullValue()));
      
    }

    /**
     * Test bad update
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeUpdateNull() {
    	clientContentSubscriptionConfigurationService.update(null);
    }

    
    /**
     * Test findByClient for null
     */
    @Test
    public void testNegativeFindNull() {
    	Page<ClientContentSubscriptionConfiguration> config = clientContentSubscriptionConfigurationService.findByClient(new Client(), null);
    	assertThat(config, is(nullValue()));
    }
    
    @Test
    public void testUnsavedReload() {
    	 Page<ContentSubscription> contentSubscription= contentSubscriptionPersistence.findActive(null);
    	 ClientContentSubscriptionConfiguration clientContentSubscription = new ClientContentSubscriptionConfiguration();
         clientContentSubscription.setClient(new Client());
         clientContentSubscription.setContentSubscription(contentSubscription.getContent().get(0));
         clientContentSubscription.setFaithBased(false);
         ClientContentSubscriptionConfiguration reloadClientContentSubscription =  clientContentSubscriptionConfigurationService.reload(clientContentSubscription);
         assertThat("reloaded is null", reloadClientContentSubscription, is(nullValue()));
    }
    
}
