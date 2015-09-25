package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ClientProgramContentInclusionPersistence;
import com.emmisolutions.emmimanager.persistence.ProgramPersistence;

import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Integration test for ClientProgramContentInclusionPersistence
 */
public class ClientProgramContentInclusionPersistenceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientProgramContentInclusionPersistence clientProgramContentInclusionPersistence;
    
    @Resource 
    ProgramPersistence programPersistence;
    
    /**
     * Test list
     */
    @Test
    public void testList() {
        Client client = makeNewRandomClient();
        ClientProgramContentInclusion contentSubscriptionConfig = new ClientProgramContentInclusion();
        contentSubscriptionConfig.setClient(client);
     

    }

}
