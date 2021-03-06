package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.configuration.team.DefaultClientTeamPhoneConfiguration;
import com.emmisolutions.emmimanager.model.schedule.Encounter;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermission;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminPermissionName;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.model.user.client.*;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermissionName;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.EncounterPersistence;
import com.emmisolutions.emmimanager.persistence.repo.UserAdminRoleRepository;
import com.emmisolutions.emmimanager.service.configuration.AsyncConfiguration;
import com.emmisolutions.emmimanager.service.configuration.MailConfiguration;
import com.emmisolutions.emmimanager.service.configuration.ServiceConfiguration;
import com.emmisolutions.emmimanager.service.configuration.ThymeleafConfiguration;
import com.emmisolutions.emmimanager.service.security.UserDetailsConfigurableAuthenticationProvider;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.service.spring.configuration.IntegrationTestConfiguration;
import com.icegreen.greenmail.spring.GreenMailBean;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.runner.RunWith;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Root integration test harness
 */
@ContextConfiguration(classes = {
        IntegrationTestConfiguration.class,
        ServiceConfiguration.class,
        AsyncConfiguration.class,
        MailConfiguration.class,
        ThymeleafConfiguration.class
})
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("test")
// @Transactional - do not enable this.. the service implementation should be
// annotated correctly!
public abstract class BaseIntegrationTest {

    @Resource
    GreenMailBean greenMailBean;

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
    UserAdminService userAdminService;

    @Resource
    UserAdminRoleRepository userAdminRoleRepository;

    @Resource
    UserClientUserClientRoleService userClientUserClientRoleService;

    @Resource
    UserClientUserClientTeamRoleService userClientUserClientTeamRoleService;

    @Resource
    UserDetailsConfigurableAuthenticationProvider authenticationProvider;

    @Resource
    UserDetailsConfigurableAuthenticationProvider adminAuthenticationProvider;
    
    @Resource
    ClientTeamPhoneConfigurationService teamPhoneConfig;
    
    @Resource
    ClientTeamEmailConfigurationService teamEmailConfig;
    
    @Resource
    ClientTeamSchedulingConfigurationService teamSchedulingConfig;

    @Resource(name = "clientUserDetailsService")
    UserDetailsService userDetailsService;

    @Resource(name = "adminUserDetailsService")
    UserDetailsService adminUserDetailsService;

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;

    @Resource
    PatientService patientService;

    @Resource
    ScheduleService scheduleService;

    @Resource
    ProgramService programService;
    
    @Resource
    EncounterPersistence encounterPersistence;

    @PostConstruct
    private void init() {
        authenticationProvider.setUserDetailsService(userDetailsService);
        adminAuthenticationProvider.setUserDetailsService(adminUserDetailsService);
    }

    /**
     * Really logs in the user
     *
     * @param login    the user's login
     * @param password the users password
     * @return the User
     * @throws org.springframework.security.core.AuthenticationException if the login fails
     */
    protected User login(String login, String password) {
        SecurityContextHolder.getContext().setAuthentication(
                authenticationProvider.authenticate(
                        new UsernamePasswordAuthenticationToken(login, password)));

        return userDetailsService.getLoggedInUser();
    }

    /**
     * Really logs in the user
     *
     * @param login    the user's login
     * @param password the users password
     * @return the User
     * @throws org.springframework.security.core.AuthenticationException if the login fails
     */
    protected User adminLogin(String login, String password) {
        SecurityContextHolder.getContext().setAuthentication(
                adminAuthenticationProvider.authenticate(
                        new UsernamePasswordAuthenticationToken(login, password)));

        return userDetailsService.getLoggedInUser();
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
     * @param group to use
     * @param count to use
     * @return a list of tags
     */
    protected List<Tag> makeNewRandomTags(Group group, int count) {
        List<Tag> tags = new ArrayList<>();
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
     * @param team to use
     * @param tags to use
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
     * Creates a brand new team that shouldn't already be inserted
     *
     * @return random team with configuration
     */
    protected Team makeNewRandomTeamWithConfiguration(Client client) {
        Team team = new Team();
        team.setName("a" + RandomStringUtils.randomAlphabetic(49));
        team.setDescription(RandomStringUtils.randomAlphabetic(50));
        team.setActive(true);
        team.setClient(client != null ? client : makeNewRandomClient());
        team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils
                .randomAlphanumeric(18)));
        teamService.create(team);
        
        ClientTeamPhoneConfiguration phoneConfig = new ClientTeamPhoneConfiguration();
        phoneConfig.setTeam(team);
        phoneConfig.setCreatedBy(new UserAdmin(1l));
        phoneConfig.setRequirePhone(true);
        phoneConfig.setCollectPhone(true);
        teamPhoneConfig.saveOrUpdate(phoneConfig);
                        
        ClientTeamEmailConfiguration emailConfig = new ClientTeamEmailConfiguration();
        emailConfig.setTeam(team);
        emailConfig.setCreatedBy(new UserAdmin(1l));
        emailConfig.setCollectEmail(true);
        emailConfig.setRequireEmail(false);
        emailConfig.setReminderTwoDays(true);
        emailConfig.setReminderFourDays(true);
        emailConfig.setReminderSixDays(true);
        emailConfig.setReminderEightDays(true);
        emailConfig.setReminderArticles(false);
        teamEmailConfig.saveOrUpdate(emailConfig);
        return team;
    }
    
    /**
     * Creates a brand new team that shouldn't already be inserted
     *
     * @return random team with scheduling configuration
     */
    protected Team makeNewRandomTeamWithSchedulingConfiguration(Client client) {
        Team team = new Team();
        team.setName("a" + RandomStringUtils.randomAlphabetic(49));
        team.setDescription(RandomStringUtils.randomAlphabetic(50));
        team.setActive(true);
        team.setClient(client != null ? client : makeNewRandomClient());
        team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils
                .randomAlphanumeric(18)));
        teamService.create(team);
        
        ClientTeamSchedulingConfiguration schedulingConfig = teamSchedulingConfig.findByTeam(team);
        teamSchedulingConfig.saveOrUpdate(schedulingConfig);
        return team;
    }


    /**
     * Create a brand new UserClientRole with given client
     * that has all permissions
     *
     * @param client to use
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
     * @param client to use
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
        userClient.setEmail(RandomStringUtils.randomAlphabetic(8) + "@" + RandomStringUtils.randomAlphabetic(10) + ".com");
        userClient.setPassword(RandomStringUtils.randomAlphanumeric(40));
        userClient.setCredentialsNonExpired(true);
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
        userClient.setTeamRoles(
                userClientUserClientTeamRoleService.associate(new ArrayList<UserClientUserClientTeamRole>() {{
                    add(userClientUserClientTeamRole);
                }})
        );

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
        userAdmin.setCredentialsNonExpired(true);
        UserAdminSaveRequest req = new UserAdminSaveRequest();
        req.setUserAdmin(userAdmin);
        req.getRoles().add(makeNewRandomUserAdminRole());
        return userAdminService.save(req);
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

    /**
     * Configures the email server to have an account for emails
     * to go
     *
     * @param email account used to accept messages
     */
    protected void setEmailMailServerUser(String email) {
        greenMailBean.getGreenMail().setUser(email, "****");
    }

    /**
     * Get all of the messages on the email server
     *
     * @return array of MimeMessage objects
     */
    protected MimeMessage[] getEmailsFromServer() {
        return greenMailBean.getReceivedMessages();
    }

    /**
     * Make a new ClientPasswordConfiguration for a passed existing client or random client
     * if null
     *
     * @param existingClient to use or null to create random new client
     * @return the persistent configuration
     */
    protected ClientPasswordConfiguration makeNewRandomClientPasswordConfiguration(Client existingClient) {
        Client client = existingClient == null ? makeNewRandomClient() : existingClient;
        ClientPasswordConfiguration configuration = clientPasswordConfigurationService
                .get(client);
        configuration.setName(RandomStringUtils.randomAlphanumeric(255));
        return clientPasswordConfigurationService.save(configuration);
    }

    /**
     * Creates a new Patient
     *
     * @param client if not null will be used to create the patient otherwise a random new client will be used
     * @return the Patient
     */
    protected Patient makeNewRandomPatient(Client client) {
        Patient patient = new Patient();
        patient.setFirstName(RandomStringUtils.randomAlphabetic(18));
        patient.setLastName(RandomStringUtils.randomAlphabetic(20));
        patient.setDateOfBirth(LocalDate.now());
        patient.setClient(client == null ? makeNewRandomClient() : client);
        patient.setEmail(RandomStringUtils.randomAlphabetic(50) + "@email.com");
        return patientService.create(patient);
    }

    /**
     * Creates a scheduled program using a new random team for the passed patient. If the
     * passed patient is null a random patient will ge created at a random client.
     *
     * @param patient to be used or null
     * @return a ScheduledProgram
     */
    protected ScheduledProgram makeNewScheduledProgram(Patient patient) {
        ScheduledProgram scheduledProgram = new ScheduledProgram();
        if (patient == null) {
            patient = makeNewRandomPatient(null);
        }
        scheduledProgram.setViewByDate(LocalDate.now(DateTimeZone.UTC).plusYears(1));
        scheduledProgram.setLocation(makeNewRandomLocation());
        scheduledProgram.setProvider(makeNewRandomProvider());
        scheduledProgram.setProgram(programService.find(null, null).iterator().next());
        scheduledProgram.setTeam(makeNewRandomTeam(patient.getClient()));
        scheduledProgram.setPatient(patient);
        return scheduleService.schedule(scheduledProgram);
    }
}
