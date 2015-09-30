package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;
import com.emmisolutions.emmimanager.model.program.ContentSubscription;
import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.ClientProgramContentInclusionPersistence;
import com.emmisolutions.emmimanager.persistence.ContentSubscriptionPersistence;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamSchedulingConfigurationPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ProgramRepository;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientContentSubscriptionConfigurationService;
import com.emmisolutions.emmimanager.service.ClientProgramContentInclusionService;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.ClientTeamSchedulingConfigurationService;
import com.emmisolutions.emmimanager.service.ProgramService;
import com.emmisolutions.emmimanager.service.TeamService;

/**
 * An integration test for ClientProgramContentInclusionServiceImpl
 */
public class ClientProgramContentInclusionServiceIntegrationTest extends
        BaseIntegrationTest {

    @Resource
    ClientProgramContentInclusionService clientProgramContentInclusionService;
    
    @Resource
    ClientProgramContentInclusionPersistence clientProgramContentInclusionPersistence;

    @Resource
    ProgramRepository programRepository;
    
    @Resource
    ProgramService programService;

     /**
     * Test CRUD
     */
    @Test
    public void testFindAndSave() {
        Client client = makeNewRandomClient();
                
        Page<Program> aProgram = programService.find(new ProgramSearchFilter().addSpecialty(new Specialty(16)), null);
        
        ClientProgramContentInclusion clientProgramContentInclusion = new ClientProgramContentInclusion();
        clientProgramContentInclusion.setClient(client);
        clientProgramContentInclusion.setProgram(aProgram.getContent().get(0));
                      
        ClientProgramContentInclusion savedClientProgramContentInclusion = clientProgramContentInclusionService.create(clientProgramContentInclusion);
        
        Page<ClientProgramContentInclusion>  findClientProgramContentInclusion = clientProgramContentInclusionService
                .findByClient(client, null);
        
        assertThat(
                "should has content subscription ",
                findClientProgramContentInclusion.hasContent()
                        , is(true));
        
        assertThat("should has item as savedClientProgramContentInclusion",
        		findClientProgramContentInclusion.getContent(), hasItem(savedClientProgramContentInclusion));
        
       
       ClientProgramContentInclusion updatedClientProgramContentInclusion = clientProgramContentInclusionService.update(clientProgramContentInclusion);
       
       clientProgramContentInclusionService.delete(updatedClientProgramContentInclusion);
       
       assertThat("delete content subscription successfully",
    		   clientProgramContentInclusionService.reload(updatedClientProgramContentInclusion), is(nullValue()));
       
       assertThat("reload will return null with null object",
    		   clientProgramContentInclusionService.reload(new ClientProgramContentInclusion()), is(nullValue()));
      
    }

    /**
     * Test findByClient for null
     */
    @Test
    public void testNegativeFindNull() {
    	Page<ClientProgramContentInclusion> programInclusion = clientProgramContentInclusionService.findByClient(new Client(), null);
    	assertThat(programInclusion, is(nullValue()));
    }
    
    @Test
    public void testUnsavedReload() {
    	Client client = makeNewRandomClient();
        Page<Program> aProgram = programService.find(new ProgramSearchFilter().addSpecialty(new Specialty(16)), null);
        ClientProgramContentInclusion clientProgramContentInclusion = new ClientProgramContentInclusion();
        clientProgramContentInclusion.setClient(client);
        clientProgramContentInclusion.setProgram(aProgram.getContent().get(0));
        ClientProgramContentInclusion reloadClientProgramContentInclusion =  clientProgramContentInclusionService.reload(clientProgramContentInclusion);
        assertThat("reloaded is null", reloadClientProgramContentInclusion, is(nullValue()));
    }
    
    @Test
    public void testDeleteNull(){
    	ClientProgramContentInclusion clientContent = null;
    	clientProgramContentInclusionService.delete(clientContent);
    	assertThat(clientContent, is(nullValue()));
    }
    
}
