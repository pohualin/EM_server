package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamEmailConfiguration;
import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.model.program.ContentSubscription;
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
        clientContentSubscription.setSource(false);
        
        ClientContentSubscriptionConfiguration savedContentSubscriptionConfig = clientContentSubscriptionConfigurationService.create(clientContentSubscription);
        
        assertThat(
                "should contain content subscription configuration faith based is false",
                savedContentSubscriptionConfig.isFaithBased()
                        , is(false));
        
        assertThat(
                "should contain content subscription configuration source is false",
                savedContentSubscriptionConfig.isSource()
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
       
    }

    /**
     * Test bad update
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void testNegativeUpdateNull() {
    	clientContentSubscriptionConfigurationService.update(null);
    }
   
    
}
