package com.emmisolutions.emmimanager.persistence.impl;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamLocation;
import com.emmisolutions.emmimanager.model.TeamProvider;
import com.emmisolutions.emmimanager.model.TeamProviderTeamLocation;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.TeamLocationPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderPersistence;
import com.emmisolutions.emmimanager.persistence.TeamProviderTeamLocationPersistence;

/**
 * TeamProviderTeamLocationPersistence integration test
 */
public class TeamProviderTeamLocationPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    TeamProviderTeamLocationPersistence teamProviderTeamLocationPersistence;

    @Resource
    TeamProviderPersistence teamProviderPersistence;

    @Resource
    TeamLocationPersistence teamLocationPersistence;

    /**
     * Test save and the first delete properly.. the rest of
     * the deletes are in there just to make sure that
     * the db queries successfully complete..
     */
    @Test
    public void save() {
        Team team = makeNewRandomTeam();
        TeamProvider tp = new TeamProvider();
        tp.setProvider(makeNewRandomProvider());
        tp.setTeam(team);
        tp = teamProviderPersistence.save(tp);

        TeamLocation tl = new TeamLocation();
        tl.setTeam(team);
        tl.setLocation(makeNewRandomLocation());
        tl = teamLocationPersistence.saveTeamLocation(tl);

        final TeamProviderTeamLocation tptl1 = new TeamProviderTeamLocation();
        tptl1.setTeamProvider(tp);
        tptl1.setTeamLocation(tl);

        List<TeamProviderTeamLocation> tptls = new ArrayList<TeamProviderTeamLocation>() {{
            add(tptl1);
        }};

        TeamProviderTeamLocation saved = teamProviderTeamLocationPersistence
            .saveAll(tptls).iterator().next();

        assertThat("Save should have worked", saved.getId(), is(notNullValue()));

        assertThat("find by TeamProvider should work",
            teamProviderTeamLocationPersistence.findByTeamProvider(tp, null),
            hasItem(tptl1));

        assertThat("find by TeamLocation should work",
            teamProviderTeamLocationPersistence.findByTeamLocation(tl, null),
            hasItem(tptl1));

        assertThat("one gets removed by TeamLocation should work",
            teamProviderTeamLocationPersistence.removeAllByTeamLocation(tl),
            is(1l));

        teamProviderTeamLocationPersistence.removeAllByTeamProvider(tp);

        teamProviderTeamLocationPersistence.removeAllByClientLocation(team.getClient(), tl.getLocation());

        teamProviderTeamLocationPersistence.removeAllByClientProvider(team.getClient(), tp.getProvider());

        teamProviderTeamLocationPersistence.delete(tptls);

    }

}
