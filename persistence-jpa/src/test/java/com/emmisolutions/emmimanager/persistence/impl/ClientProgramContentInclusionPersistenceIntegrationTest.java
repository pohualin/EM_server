package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;
import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.Specialty;
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
        ProgramSearchFilter filter =new ProgramSearchFilter();
        filter.addSpecialty(new Specialty(16));
        filter.client(client);
        Page<Program> aProgram = programPersistence.find(filter, null);
        
        ClientProgramContentInclusion contentInclusion = new ClientProgramContentInclusion();
        contentInclusion.setClient(client);
        contentInclusion.setProgram(aProgram.getContent().get(0));
      
       
        ClientProgramContentInclusion savedContentProgram = clientProgramContentInclusionPersistence.saveOrUpdate(contentInclusion);

        Page<ClientProgramContentInclusion> clientContentProgramList = clientProgramContentInclusionPersistence
        		.findByClient(client.getId(), null);
       

        assertThat("saved config should be found", clientContentProgramList, 
        		hasItem(savedContentProgram));

        ClientProgramContentInclusion configurationReload = clientProgramContentInclusionPersistence
                .reload(savedContentProgram.getId());

        assertThat("reload should be the same configuration as the saved one", configurationReload,
                is(savedContentProgram));
        
        clientProgramContentInclusionPersistence.delete(savedContentProgram.getId());
        
        assertThat("delete the saved content program successfully",
        		clientProgramContentInclusionPersistence.reload(savedContentProgram.getId()), is(nullValue()));

    }


}
