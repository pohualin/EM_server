package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.configuration.CacheConfiguration;
import com.emmisolutions.emmimanager.persistence.configuration.PersistenceConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Root integration test harness
 */
@ContextConfiguration(classes = {PersistenceConfiguration.class, CacheConfiguration.class})
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("test")
@Transactional
public abstract class BaseIntegrationTest {

    @Resource
    UserAdminPersistence userAdminPersistence;

    @Resource
    GroupPersistence groupPersistence;

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    TagPersistence tagPersistence;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    TeamTagPersistence teamTagPersistence;

    @Resource
    ProviderPersistence providerPersistence;

    @Resource
    LocationPersistence locationPersistence;

    @Resource
    UserClientRolePersistence userClientRolePersistence;

    @Resource
    UserClientTeamRolePersistence userClientTeamRolePersistence;

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    PatientPersistence patientPersistence;

    @Resource
    SchedulePersistence schedulePersistence;

    @Resource
    ProgramPersistence programPersistence;

    /**
     * Login as a user
     *
     * @param login to login as
     */
    protected void login(String login) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(login, "******"));
    }

    /**
     * Logout of the system..
     */
    protected void logout() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Creates a brand new client that shouldn't already be inserted
     *
     * @return random client
     */
    protected Client makeNewRandomClient() {
        Client client = new Client();
        client.setTier(new ClientTier(3l));
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setContractStart(LocalDate.now());
        client.setRegion(new ClientRegion(1l));
        client.setName(RandomStringUtils.randomAlphanumeric(255));
        client.setType(new ClientType(1l));
        client.setActive(true);
        client.setContractOwner(makeNewRandomUserAdmin());
        client.setSalesForceAccount(new SalesForce(RandomStringUtils
                .randomAlphanumeric(18)));
        return clientPersistence.save(client);
    }

    /**
     * Creates a brand new group
     *
     * @param client to use
     * @return random group
     */
    protected Group makeNewRandomGroup(Client client) {
        Group group = new Group();
        group.setName(RandomStringUtils.randomAlphabetic(10));
        group.setClient(client != null ? client : makeNewRandomClient());
        return groupPersistence.save(group);
    }

    /**
     * Create a list of tags with given group
     *
     * @param group to use
     * @param count to use
     * @return list of tags
     */
    protected List<Tag> makeNewRandomTags(Group group, int count) {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Tag tag = new Tag();
            tag.setName(RandomStringUtils.randomAlphabetic(10));
            tag.setGroup(group != null ? group : makeNewRandomGroup(null));
            tags.add(tag);
        }
        return tagPersistence.createAll(tags);
    }

    /**
     * Create a new TeamTag
     *
     * @param team to use
     * @param tag  to use
     * @return a new teamTag
     */
    protected TeamTag makeNewTeamTag(Team team, Tag tag) {
        TeamTag teamTag = new TeamTag(team, tag);
        return teamTagPersistence.saveTeamTag(teamTag);
    }

    /**
     * Create a new random team
     *
     * @param client to use
     * @return random team
     */
    protected Team makeNewRandomTeam(Client client) {
        Team team = new Team();
        team.setName(RandomStringUtils.randomAlphanumeric(100));
        team.setClient(client != null ? client : makeNewRandomClient());
        team.setActive(true);
        team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils
                .randomAlphanumeric(18)));
        team.setDescription(RandomStringUtils.randomAlphabetic(255));
        return teamPersistence.save(team);
    }

    /**
     * Make a new randomized Provider
     *
     * @return random provider
     */
    protected Provider makeNewRandomProvider() {
        Provider provider = new Provider();
        provider.setFirstName(RandomStringUtils.randomAlphabetic(255));
        provider.setLastName(RandomStringUtils.randomAlphabetic(255));
        provider.setActive(true);
        return providerPersistence.save(provider);
    }

    /**
     * Create a new randomized location
     *
     * @return the saved location
     */
    protected Location makeNewRandomLocation() {
        Location location = new Location();
        location.setActive(true);
        location.setName(RandomStringUtils.randomAlphanumeric(255));
        location.setCity(RandomStringUtils.randomAlphanumeric(255));
        location.setPhone("214-555-5555");
        location.setState(State.TX);
        return locationPersistence.save(location);
    }

    /**
     * Creates a new UserClient
     *
     * @return a UserClient
     */
    protected UserClient makeNewRandomUserClient(Client client) {
        UserClient userClient = new UserClient();
        userClient.setClient(client != null ? client : makeNewRandomClient());
        userClient.setFirstName("a" + RandomStringUtils.randomAlphabetic(49));
        userClient.setLastName(RandomStringUtils.randomAlphabetic(50));
        userClient.setLogin(RandomStringUtils.randomAlphabetic(255));
        userClient.setSecretQuestionCreated(false);
        userClient.setEmail(RandomStringUtils.randomAlphabetic(25) + "@" + RandomStringUtils.randomAlphabetic(25) + ".com");
        return userClientPersistence.saveOrUpdate(userClient);
    }

    /**
     * Creates a new UserClientRole
     *
     * @return a UserClientRole
     */
    protected UserClientRole makeNewRandomUserClientRole(Client client) {
        if (client == null) {
            client = makeNewRandomClient();
        }
        UserClientRole userClientRole = new UserClientRole(
                RandomStringUtils.randomAlphabetic(10), client, null);
        return userClientRolePersistence.save(userClientRole);
    }

    /**
     * Create a random UserClientTeamRole
     */
    protected UserClientTeamRole makeNewRandomUserClientTeamRole(Client client) {
        if (client == null) {
            client = makeNewRandomClient();
        }
        UserClientTeamRole userClientTeamRole = new UserClientTeamRole();
        userClientTeamRole.setName(RandomStringUtils.randomAlphabetic(50));
        userClientTeamRole.setClient(client);
        return userClientTeamRolePersistence.save(userClientTeamRole);
    }

    /**
     * Creates a new UserAdmin
     *
     * @return a UserAdmin
     */
    protected UserAdmin makeNewRandomUserAdmin() {
        UserAdmin userAdmin = new UserAdmin(
                RandomStringUtils.randomAlphabetic(255),
                RandomStringUtils.randomAlphanumeric(40));
        userAdmin.setFirstName(RandomStringUtils.randomAlphabetic(50));
        userAdmin.setLastName(RandomStringUtils.randomAlphabetic(50));
        return userAdminPersistence.saveOrUpdate(userAdmin);
    }

    /**
     * Creates a new patient
     *
     * @param client or null to create a random client
     * @return a Patient
     */
    protected Patient makeNewRandomPatient(Client client) {
        Patient patient = new Patient();
        patient.setFirstName(RandomStringUtils.randomAlphabetic(18));
        patient.setLastName(RandomStringUtils.randomAlphabetic(20));
        patient.setEmail(RandomStringUtils.randomAlphabetic(25) + "@" + RandomStringUtils.randomAlphabetic(25) + ".com");
        patient.setDateOfBirth(LocalDate.now());
        patient.setPhone("3" + RandomStringUtils.randomNumeric(2) + "-4" +
                RandomStringUtils.randomNumeric(2) + "-" +
                RandomStringUtils.randomNumeric(4));
        patient.setClient(client == null ? makeNewRandomClient() : client);
        return patientPersistence.save(patient);
    }

    /**
     * Creates a new scheduled program
     *
     * @param client  to use or null to make a random
     * @param patient to use or null to make a random patient on the client
     * @param team    or null to create a new random team on the client, you probably
     *                shouldn't use a Team with a different Client than the passed Client.
     *                You can of course, but the data isn't really valid. Also our service
     *                layer prohibits this case.
     * @return a ScheduledProgram
     */
    public ScheduledProgram makeNewRandomScheduledProgram(Client client, Patient patient, Team team) {
        ScheduledProgram scheduledProgram = new ScheduledProgram();
        if (client == null) {
            client = makeNewRandomClient();
        }
        if (patient == null) {
            patient = makeNewRandomPatient(client);
        }
        if (team == null) {
            team = makeNewRandomTeam(client);
        }
        scheduledProgram.setAccessCode("2" + RandomStringUtils.randomNumeric(10));
        scheduledProgram.setViewByDate(LocalDate.now(DateTimeZone.UTC));
        scheduledProgram.setLocation(makeNewRandomLocation());
        scheduledProgram.setProvider(makeNewRandomProvider());
        scheduledProgram.setProgram(programPersistence.find(null, null).iterator().next());
        scheduledProgram.setTeam(team);
        scheduledProgram.setPatient(patient);
        return schedulePersistence.save(scheduledProgram);
    }
}
