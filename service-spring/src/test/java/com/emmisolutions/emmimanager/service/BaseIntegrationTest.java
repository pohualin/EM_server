package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermission;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.model.user.client.*;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermissionName;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminRoleRepository;
import com.emmisolutions.emmimanager.service.configuration.ServiceConfiguration;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Resource
    UserAdminRoleRepository userAdminRoleRepository;

    @Resource
    UserClientUserClientRoleService userClientUserClientRoleService;

    @Resource
    UserClientUserClientTeamRoleService userClientUserClientTeamRoleService;

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
     * that has all permissions
     * 
     * @param client
     *            to use
     * @return random UserClientRole
     */
    protected UserClientRole makeNewRandomUserClientRole(Client client) {
        if (client == null) {
            client = makeNewRandomClient();
        }
        Set<UserClientPermission> userClientPermissions = new HashSet<>();
        for (UserClientPermissionName userClientPermissionName : UserClientPermissionName.values()) {
            userClientPermissions.add(new UserClientPermission(userClientPermissionName));
        }
        UserClientRole userClientRole = new UserClientRole(
                RandomStringUtils.randomAlphabetic(10), client, userClientPermissions);
        return userClientRoleService.create(userClientRole);
    }

    /**
     * Create a brand new UserClientTeamRole with given client
     * with all possible permissions
     * 
     * @param client
     *            to use
     * @return random UserClientTeamRole
     */
    protected UserClientTeamRole makeNewRandomUserClientTeamRole(Client client) {
        if (client == null) {
            client = makeNewRandomClient();
        }
        Set<UserClientTeamPermission> userClientTeamPermissions = new HashSet<>();
        for (UserClientTeamPermissionName userClientTeamPermissionName : UserClientTeamPermissionName.values()) {
            userClientTeamPermissions.add(new UserClientTeamPermission(userClientTeamPermissionName));
        }
        UserClientTeamRole userClientTeamRole = new UserClientTeamRole(
                RandomStringUtils.randomAlphabetic(10), client, userClientTeamPermissions);
        return userClientTeamRoleService.create(userClientTeamRole);
    }

    /**
     * Creates a new UserClient, user client role, team and user client team role.
     * Then puts the user client in the new client and team roles.
     *
     * @return a UserClient
     */
    protected UserClient makeNewRandomUserClient(Client client) {
        // create the user
        UserClient userClient = new UserClient();
        userClient.setClient(client != null ? client : makeNewRandomClient());
        userClient.setFirstName("a" + RandomStringUtils.randomAlphabetic(49));
        userClient.setLastName(RandomStringUtils.randomAlphabetic(50));
        userClient.setLogin(RandomStringUtils.randomAlphabetic(255));
        userClient.setPassword(RandomStringUtils.randomAlphanumeric(40));
        UserClient savedUserClient = userClientService.create(userClient);

        // put the user client in a new client role with all permissions
        UserClientUserClientRole userClientUserClientRole = new UserClientUserClientRole();
        userClientUserClientRole.setUserClientRole(makeNewRandomUserClientRole(client));
        userClientUserClientRole.setUserClient(savedUserClient);
        userClientUserClientRoleService.create(userClientUserClientRole);

        // put the user client in a new team role with all permission for a random team in the client
        final UserClientUserClientTeamRole userClientUserClientTeamRole = new UserClientUserClientTeamRole();
        userClientUserClientTeamRole.setUserClient(userClient);
        userClientUserClientTeamRole.setUserClientTeamRole(makeNewRandomUserClientTeamRole(client));
        userClientUserClientTeamRole.setTeam(makeNewRandomTeam(client));
        userClientUserClientTeamRoleService.associate(new ArrayList<UserClientUserClientTeamRole>() {{
            add(userClientUserClientTeamRole);
        }});

        return savedUserClient;
    }

    /**
     * Make new random UserAdmin
     *
     * @return new UserAdmin
     */
    protected UserAdmin makeNewRandomUserAdmin() {
        UserAdmin userAdmin = new UserAdmin(
                RandomStringUtils.randomAlphabetic(255),
                RandomStringUtils.randomAlphanumeric(40));
        userAdmin.setFirstName(RandomStringUtils.randomAlphabetic(10));
        userAdmin.setLastName(RandomStringUtils.randomAlphabetic(10));
        UserAdminSaveRequest req = new UserAdminSaveRequest();
        req.setUserAdmin(userAdmin);
        req.getRoles().add(makeNewRandomUserAdminRole());
        return userService.save(req);
    }

    /**
     * Creates a brand new user admin role to be used
     *
     * @return the persistent user admin role with the PERM_ADMIN_USER permission
     */
    protected UserAdminRole makeNewRandomUserAdminRole() {
        UserAdminRole userAdminRole = new UserAdminRole();
        userAdminRole.setName(RandomStringUtils.randomAlphanumeric(255));
        Set<UserAdminPermission> userAdminPermissions = new HashSet<>();
        for (UserAdminPermissionName userAdminPermissionName : UserAdminPermissionName.values()) {
            userAdminPermissions.add(new UserAdminPermission(userAdminPermissionName));
        }
        userAdminRole.setPermissions(userAdminPermissions);
        return userAdminRoleRepository.save(userAdminRole);
    }

}
