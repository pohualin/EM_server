package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 *  TeamProviderPersistence integration test
 */
public class TeamProviderPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    TeamProviderPersistence teamProviderPersistence;

    /**
     * Testing a provider save without a persistent team.
     */
    @Test(expected = ConstraintViolationException.class)
    public void invalidSave() {
        TeamProvider tp = new TeamProvider();
        teamProviderPersistence.save(tp);
    }

    /**
     * Save, reload and find by various ways, and delete single
     */
    @Test
    public void save() {
        TeamProvider tp = new TeamProvider();
        tp.setTeam(makeNewRandomTeam());
        tp.setProvider(makeNewRandomProvider());
        TeamProvider saved = teamProviderPersistence.save(tp);
        assertThat("save was successful",
            saved, is(notNullValue()));

        assertThat("reload works", teamProviderPersistence.reload(saved.getId()), is(saved));

        assertThat("find teams by client and provider works",
            teamProviderPersistence.findTeamsBy(saved.getTeam().getClient(), saved.getProvider(), null),
            hasItem(saved.getTeam()));

        assertThat("find team provider by team and provider works",
            teamProviderPersistence.findTeamProvider(saved.getTeam(), saved.getProvider()),
            is(saved));

        assertThat("find team providers by team works",
            teamProviderPersistence.findTeamProvidersByTeam(null, saved.getTeam()),
            hasItem(saved));

        teamProviderPersistence.delete(saved);
        assertThat("ensure deleted", teamProviderPersistence.reload(saved.getId()), is(nullValue()));
    }

    /**
     * Save a couple of TPs and then delete them
     */
    @Test
    public void saveAll() {
        final TeamProvider one = new TeamProvider();
        Team team = makeNewRandomTeam();
        one.setTeam(team);
        one.setProvider(makeNewRandomProvider());
        final TeamProvider two = new TeamProvider();
        two.setTeam(team);
        two.setProvider(makeNewRandomProvider());

        assertThat("save team providers by team works",
            teamProviderPersistence.saveAll(new ArrayList<TeamProvider>() {{
                add(one);
                add(two);
            }}),
            hasItems(one, two));

        assertThat("find team providers by team works",
            teamProviderPersistence.findTeamProvidersByTeam(new PageRequest(0,10), team),
            hasItems(one, two));

        assertThat("delete happens for only one provider for the client",
            teamProviderPersistence.delete(team.getClient(), two.getProvider()),
            is(1l));
    }

}