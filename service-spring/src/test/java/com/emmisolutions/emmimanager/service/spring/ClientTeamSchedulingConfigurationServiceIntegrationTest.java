package com.emmisolutions.emmimanager.service.spring;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
        schedulingConfig = clientTeamSchedulingConfigurationService
                .saveOrUpdate(schedulingConfig);

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

    /**
     * Test value out of range
     */
    @Test
    public void testOutofRange() {
        Client client = makeNewRandomClient();
        Team team = makeNewRandomTeam(client);
        ClientTeamSchedulingConfiguration schedulingConfig = clientTeamSchedulingConfigurationService
                .findByTeam(team);

        // greater than max
        try {
            schedulingConfig.setViewByDays(500);
            clientTeamSchedulingConfigurationService
                    .saveOrUpdate(schedulingConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // less than min
        try {
            schedulingConfig.setViewByDays(-5);
            clientTeamSchedulingConfigurationService
                    .saveOrUpdate(schedulingConfig);
            fail("did not throw when it should");
        } catch (InvalidDataAccessApiUsageException e) {
        }

        // in range
        schedulingConfig.setViewByDays(360);
        schedulingConfig = clientTeamSchedulingConfigurationService.saveOrUpdate(schedulingConfig);
        schedulingConfig.setViewByDays(0);
        schedulingConfig = clientTeamSchedulingConfigurationService.saveOrUpdate(schedulingConfig);

    }

}
