package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter.StatusFilter;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 *  TeamProviderPersistence integration test
 */
public class TeamProviderPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    TeamProviderPersistence teamProviderPersistence;
    @Resource
    ProviderPersistence providerPersistence;
    /**
     * Testing a provider save without a persistent team.
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
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
        tp.setTeam(makeNewRandomTeam(null));
        tp.setProvider(makeNewRandomProvider());
        TeamProvider saved = teamProviderPersistence.save(tp);
        assertThat("save was successful",
            saved, is(notNullValue()));

        assertThat("reload works", teamProviderPersistence.reload(saved.getId()), is(saved));

        assertThat("find teams by client and provider works",
            teamProviderPersistence.findTeamsBy(saved.getTeam().getClient(), saved.getProvider(), null),
            hasItem(saved.getTeam()));

        assertThat("find team providers by team works",
            teamProviderPersistence.findTeamProvidersByTeam(null, saved.getTeam()),
            hasItem(saved));

        ProviderSearchFilter filter = new ProviderSearchFilter(StatusFilter.ACTIVE_ONLY, "a");
        assertThat("find by teamId and provider works", teamProviderPersistence.getByTeamIdAndProviders(saved.getTeam().getId(), providerPersistence.list(null, filter)), is(notNullValue()));

        assertThat("find by teamId and provider when passed in nulls works", teamProviderPersistence.getByTeamIdAndProviders(null, null).size(), is(0));

        assertThat("find by provider and team should return same teamprovider",
                teamProviderPersistence.findTeamProvidersByProviderAndTeam(null, tp.getProvider(), tp.getTeam()),
                is(saved));

        teamProviderPersistence.delete(saved);
        assertThat("ensure deleted", teamProviderPersistence.reload(saved.getId()), is(nullValue()));
    }

    /**
     * Save a couple of TPs and then delete them
     */
    @Test
    public void saveAll() {
        final TeamProvider one = new TeamProvider();
        Team team = makeNewRandomTeam(null);
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
    
    /**
     * This test is so that when a client passes just the id of the provider, the association between the team and the provider is still made.
     *  
     */
    @Test
    public void saveTeamProviderWithOnlyProviderIdSet() {        
        Team team = makeNewRandomTeam(null);
        Provider provider = makeNewRandomProvider();
        
        TeamProvider tp = new TeamProvider(new Team(team.getId()), new Provider(provider.getId()));
        TeamProvider saved = teamProviderPersistence.save(tp);
        assertThat("save was successful",
            saved, is(notNullValue()));
    }

}
