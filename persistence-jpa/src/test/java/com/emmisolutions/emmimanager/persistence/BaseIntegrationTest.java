package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.persistence.configuration.PersistenceConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
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

/**
 * Root integration test harness
 */
@ContextConfiguration(classes = PersistenceConfiguration.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = true)
@ActiveProfiles("test")
@Transactional
public abstract class BaseIntegrationTest {

    @Resource
    UserPersistence userPersistence;

    @Resource
    ClientPersistence clientPersistence;

    @Resource
    TeamPersistence teamPersistence;

    @Resource
    ProviderPersistence providerPersistence;

    @Resource
    LocationPersistence locationPersistence;

    /**
     * Login as a user
     * @param login to login as
     */
    protected void login(String login) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(login, "******"));
    }

    /**
     * Logout of the system..
     */
    protected void logout(){
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
        client.setSalesForceAccount(new SalesForce(RandomStringUtils.randomAlphanumeric(18)));
        return clientPersistence.save(client);
    }

    /**
     * Makes a new randomized team
     *
     * @return random team
     */
    protected Team makeNewRandomTeam() {
        Team team = new Team();
        team.setName(RandomStringUtils.randomAlphanumeric(100));
        team.setClient(makeNewRandomClient());
        team.setActive(true);
        team.setSalesForceAccount(new TeamSalesForce(RandomStringUtils.randomAlphanumeric(18)));
        team.setDescription(RandomStringUtils.randomAlphabetic(255));
        return teamPersistence.save(team);
    }

    /**
     * Make a new randomized Provider
     * @return random provider
     */
    protected Provider makeNewRandomProvider(){
        Provider provider = new Provider();
        provider.setFirstName(RandomStringUtils.randomAlphabetic(255));
        provider.setLastName(RandomStringUtils.randomAlphabetic(255));
        provider.setActive(true);
        return providerPersistence.save(provider);
    }

    /**
     * Create a new randomized location
     * @return the saved location
     */
    protected Location makeNewRandomLocation(){
        Location location = new Location();
        location.setActive(true);
        location.setName(RandomStringUtils.randomAlphanumeric(255));
        location.setCity(RandomStringUtils.randomAlphanumeric(255));
        location.setPhone("214-555-5555");
        location.setState(State.TX);
        return locationPersistence.save(location);
    }

    /**
     * Creates a new UserAdmin
     *
     * @return a UserAdmin
     */
    protected UserAdmin makeNewRandomUserAdmin() {
        UserAdmin userAdmin = new UserAdmin(RandomStringUtils
            .randomAlphabetic(255), RandomStringUtils
            .randomAlphanumeric(100));
        userAdmin.setFirstName(RandomStringUtils.randomAlphabetic(10));
        userAdmin.setLastName(RandomStringUtils.randomAlphabetic(10));
        return userPersistence.saveOrUpdate(userAdmin);
    }

}
