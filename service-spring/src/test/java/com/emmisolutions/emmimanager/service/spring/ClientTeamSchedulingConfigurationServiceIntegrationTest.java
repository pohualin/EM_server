package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientTeamSchedulingConfiguration;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.DefaultClientTeamSchedulingConfigurationPersistence;
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

    @Resource
    DefaultClientTeamSchedulingConfigurationPersistence defaultClientTeamSchedulingConfigurationPersistence;

    /**
     * Test CRUD
     */
    @Test
    public void testFindAndSave() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);

        ClientTeamSchedulingConfiguration schedulingConfig = clientTeamSchedulingConfigurationService
                .findByTeam(team);
        
        assertThat(
                "should contain default client team scheduling configuration",
                schedulingConfig.getDefaultClientTeamSchedulingConfiguration()
                        .isActive(), is(true));
        assertThat(
                "should contain client team scheduling configuration with default value",
                schedulingConfig.isUseLocation(), is(true));

        schedulingConfig.setUseLocation(false);
        schedulingConfig = clientTeamSchedulingConfigurationService.saveOrUpdate(schedulingConfig);
        
        assertThat(
                "should contain client team scheduling configuration with isUseLocation false",
                schedulingConfig.isUseLocation(), is(false));
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
