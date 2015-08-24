package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientContentSubscriptionConfiguration;
import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientContentSubscriptionConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.ContentSubscriptionPersistence;

import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Integration test for ClientContentSubscriptionConfigurationPersistence
 */
public class ClientContentSubscriptionConfigurationPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientContentSubscriptionConfigurationPersistence clientContentSubscriptionConfigurationPersistence;
    
    @Resource
    ContentSubscriptionPersistence contentSubscriptionPersistence;

    
    /**
     * Test list
     */
    @Test
    public void testList() {
        Client client = makeNewRandomClient();
        Page<ContentSubscription> systemDefault = contentSubscriptionPersistence
                .findActive(null);
        
        ClientContentSubscriptionConfiguration contentSubscriptionConfig = new ClientContentSubscriptionConfiguration();
        contentSubscriptionConfig.setClient(client);
        contentSubscriptionConfig.setContentSubscription(systemDefault.getContent().get(3));
        contentSubscriptionConfig.setSource(false);
        contentSubscriptionConfig.setFaithBased(false);
        contentSubscriptionConfig.setSourceContentSubscription(null);
        
        ClientContentSubscriptionConfiguration savedContentSubscriptionConfig = clientContentSubscriptionConfigurationPersistence.saveOrUpdate(contentSubscriptionConfig);

        Page<ClientContentSubscriptionConfiguration> configurationSave = clientContentSubscriptionConfigurationPersistence
        		.findByClient(client.getId(), null);
       

        assertThat("should contain configuration", savedContentSubscriptionConfig,
                is(configurationSave.getContent().get(0)));

        ClientContentSubscriptionConfiguration configurationReload = clientContentSubscriptionConfigurationPersistence
                .reload(savedContentSubscriptionConfig.getId());

        assertThat("should reload the same configuration", configurationSave.getContent().get(0),
                is(configurationReload));
        
        clientContentSubscriptionConfigurationPersistence.delete(savedContentSubscriptionConfig.getId());
        
        assertThat("delete note successfully",
        		clientContentSubscriptionConfigurationPersistence.reload(savedContentSubscriptionConfig.getId()), is(nullValue()));

    }

}
