package com.emmisolutions.emmimanager.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDate;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.ClientRegion;
import com.emmisolutions.emmimanager.model.ClientTier;
import com.emmisolutions.emmimanager.model.ClientType;
import com.emmisolutions.emmimanager.model.Group;
import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.State;
import com.emmisolutions.emmimanager.model.Tag;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.model.TeamSalesForce;
import com.emmisolutions.emmimanager.model.TeamTag;
import com.emmisolutions.emmimanager.model.UserAdminSaveRequest;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.service.configuration.ServiceConfiguration;

/**
 * Root integration test harness
 */
@ContextConfiguration(classes = ServiceConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("test")
// @Transactional - do not enable this.. the service implementation should be
// annotated correctly!
public abstract class BaseIntegrationTest {

    @Resource
    ClientService clientService;

    @Resource
    GroupService groupService;

    @Resource
    LocationService locationService;

    @Resource
    ProviderService providerService;

    @Resource
    TagService tagService;

    @Resource
    TeamService teamService;

    @Resource
    TeamTagService teamTagService;

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientRoleService userClientRoleService;

    @Resource
    UserClientTeamRoleService userClientTeamRoleService;

    @Resource
    UserService userService;

    /**
     * Login as a user
     *
     * @param login
     *            to login as
     */
    protected void login(String login) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(login, "******"));
    }

    /**
     * Makes a UserDetails object with authorities
     *
     * @param login
     *            to use
     */
    protected void login(String login, List<GrantedAuthority> authorityList) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new User(login, "****",
                        authorityList), "******"));
    }

    /**
     * Logout of the system
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
        return clientService.create(client);
    }

    /**
     * Creates a brand new group
     * 
     * @return random group
     */
    protected Group makeNewRandomGroup(Client client) {
        Group group = new Group();
        group.setName(RandomStringUtils.randomAlphabetic(10));
        group.setClient(client != null ? client : makeNewRandomClient());
        return groupService.save(group);
    }

    /**
     * Creates a brand new location that shouldn't already be inserted
     *
     * @return random location
     */
    protected Location makeNewRandomLocation() {
        Location location = new Location();
        location.setName(RandomStringUtils.randomAlphabetic(50));
        location.setCity(RandomStringUtils.randomAlphabetic(50));
        location.setActive(true);
        location.setPhone("555-422-1212");
        location.setState(State.IL);
        return locationService.create(location);
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
        return providerService.create(provider);
    }

    /**
     * Make a list of random Tags
     * 
     * @param group
     *            to use
     * @param count
     *            to use
     * @return a list of tags
     */
    protected List<Tag> makeNewRandomTags(Group group, int count) {
        List<Tag> tags = new ArrayList<Tag>();
        for (int i = 0; i < count; i++) {
            Tag tag = new Tag();
            tag.setName(RandomStringUtils.randomAlphabetic(10));
            tag.setGroup(group != null ? group : makeNewRandomGroup(null));
            tags.add(tag);
        }
        return tagService.saveAllTagsForGroup(tags, group);
    }

    /**
     * Make a list of TeamTags
     * 
     * @param team
     *            to use
     * @param tags
     *            to use
     * @return list of teamTags
     */
    protected List<TeamTag> makeNewTeamTags(Team team, Set<Tag> tags) {
        return teamTagService.save(team, tags);
    }

    /**
     * Creates a brand new team that shouldn't already be inserted
     *
     * @return random team
     */
    protected Team makeNewRandomTeam(Client client) {
        Team team = new Team();
        team.setName("a" + RandomStringUtils.randomAlphabetic(49));
        team.setDescription(RandomStringUtils.randomAlphabetic(50));
        team.setActive(true);
        team.setClient(client != null ? client : makeNewRandomClient());
        team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils
                .randomAlphanumeric(18)));
        return teamService.create(team);
    }

    /**
     * Create a brand new UserClientRole with given client
     * 
     * @param client
     *            to use
     * @return random UserClientRole
     */
    protected UserClientRole makeNewRandomUserClientRole(Client client) {
        if (client == null) {
            client = makeNewRandomClient();
        }
        UserClientRole userClientRole = new UserClientRole(
                RandomStringUtils.randomAlphabetic(10), client, null);
        return userClientRoleService.create(userClientRole);
    }

    /**
     * Create a brand new UserClientTeamRole with given client
     * 
     * @param client
     *            to use
     * @return random UserClientTeamRole
     */
    protected UserClientTeamRole makeNewRandomUserClientTeamRole(Client client) {
        if (client == null) {
            client = makeNewRandomClient();
        }
        UserClientTeamRole userClientTeamRole = new UserClientTeamRole(
                RandomStringUtils.randomAlphabetic(10), client, null);
        return userClientTeamRoleService.create(userClientTeamRole);
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
        userClient.setPassword(RandomStringUtils.randomAlphanumeric(100));
        return userClientService.create(userClient);
    }

    /**
     * Make new random UserAdmin
     *
     * @return new UserAdmin
     */
    protected UserAdmin makeNewRandomUserAdmin() {
        UserAdmin userAdmin = new UserAdmin(
                RandomStringUtils.randomAlphabetic(255),
                RandomStringUtils.randomAlphanumeric(100));
        userAdmin.setFirstName(RandomStringUtils.randomAlphabetic(10));
        userAdmin.setLastName(RandomStringUtils.randomAlphabetic(10));
        UserAdminSaveRequest req = new UserAdminSaveRequest();
        req.setUserAdmin(userAdmin);
        req.setRoles(new HashSet<UserAdminRole>());
        return userService.save(req);
    }
}
