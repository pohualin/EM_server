package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.model.salesforce.CaseForm;
import com.emmisolutions.emmimanager.model.salesforce.IdNameLookupResult;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.SalesForceService;
import com.emmisolutions.emmimanager.service.UserAdminService;
import org.joda.time.LocalDate;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests sales force lookup service
 */
public class SalesForceLookupServiceImplTest extends BaseIntegrationTest {

    @Resource
    SalesForceService salesForceService;

    @Resource
    ClientService clientService;

    @Resource
    UserAdminService userAdminService;

    /**
     * This is a full stack kind of test: find a real sf account, make a client using
     * that sf account, associate a new sf account to the client, then make sure
     * the first sf account was removed.
     */
    @Test
    public void lifecycle() {
        String uniqueName = "Abbott Northwestern Hospital Heart Hospital";

        // search salesforce to make sure this account exists
        SalesForceSearchResponse salesForceSearchResponse = salesForceService.find(uniqueName);
        assertThat("response has a result", salesForceSearchResponse.getAccounts().size(), is(1));
        assertThat("response does not have an id", salesForceSearchResponse.getAccounts().get(0).getId(), is(nullValue()));

        // create a client with this sf account number
        Client client = clientService.create(makeClient("clientName", salesForceSearchResponse.getAccounts().get(0).getAccountNumber()));

        // ensure that the find returns the client name
        SalesForce sf = client.getSalesForceAccount();
        salesForceSearchResponse = salesForceService.find(uniqueName);
        assertThat("response has same id as persisted account", salesForceSearchResponse.getAccounts().get(0).getId(), is(sf.getId()));
        assertThat("response has same version as persisted account", salesForceSearchResponse.getAccounts().get(0).getVersion(), is(sf.getVersion()));
        assertThat("client name is correct", salesForceSearchResponse.getAccounts().get(0).getClientName(), is("clientName"));

        // now mod the sf account on the client by creating a new one
        client.setSalesForceAccount(new SalesForce("NEWSALESFORCEACCNT"));
        client = clientService.update(client);
        assertThat("new sf account was persisted", client.getSalesForceAccount().getId(), is(notNullValue()));

        //ensure that the deletion of the first SalesForce object has happened
        salesForceSearchResponse = salesForceService.find(uniqueName);
        assertThat("response has same id as persisted account", salesForceSearchResponse.getAccounts().get(0).getId(), is(nullValue()));
        assertThat("response has same version as persisted account", salesForceSearchResponse.getAccounts().get(0).getVersion(), is(nullValue()));
        assertThat("client name is correct", salesForceSearchResponse.getAccounts().get(0).getClientName(), is(nullValue()));
    }

    /**
     * Make sure we call salesforce save case, but instead of actually creating a case
     * just make sure that without a type things don't save
     */
    @Test
    public void saveCase() {
        CaseForm aCase = salesForceService.blankFormFor(salesForceService.possibleCaseTypes().get(0));
        aCase.setType(null);
        assertThat("blank case type doesn't save", salesForceService.saveCase(aCase).isSuccess(), is(false));
    }

    /**
     * Query SF by name and type
     */
    @Test
    public void findByNameInTypes() {
        assertThat("find by name for an account type should yield results",
                salesForceService.findByNameInTypes("magee", 1, "account").hasResults(),
                is(true));
    }

    /**
     * Ensures that possible SF accounts can be found for a UserClient
     */
    @Test
    public void findPossibleAccounts() {
        UserClient userClient = makeNewRandomUserClient(null);
        List<IdNameLookupResult> shouldBeFound = new ArrayList<>();
        shouldBeFound.add(new IdNameLookupResult(userClient.getClient().getSalesForceAccount().getAccountNumber(), ""));
        for (UserClientUserClientTeamRole teamRole : userClient.getTeamRoles()) {
            shouldBeFound.add(new IdNameLookupResult(teamRole.getTeam().getSalesForceAccount().getAccountNumber(), ""));
        }

        List<IdNameLookupResult> sfAccounts = salesForceService.possibleAccounts(new UserClient(userClient.getId()));
        assertThat("Client and all teams user has a role for should be found when searching by the user client",
                sfAccounts,
                hasItems(shouldBeFound.toArray(new IdNameLookupResult[shouldBeFound.size()])));
    }

    @Test
    public void findPossibleAccountsForPatient() {
        Patient patient = makeNewRandomPatient(null);
        List<IdNameLookupResult> shouldBeFound = new ArrayList<>();
        shouldBeFound.add(new IdNameLookupResult(patient.getClient().getSalesForceAccount().getAccountNumber(), ""));
        for (int i = 0; i < 5; i++) {
            shouldBeFound.add(
                    new IdNameLookupResult(
                            makeNewScheduledProgram(patient).getTeam().getSalesForceAccount().getAccountNumber(), ""));
        }
        List<IdNameLookupResult> sfAccounts = salesForceService.possibleAccounts(new Patient(patient.getId()));
        assertThat("Patient client and all teams on which a patient was scheduled for a program should appear",
                sfAccounts,
                hasItems(shouldBeFound.toArray(new IdNameLookupResult[shouldBeFound.size()])));
    }


    private Client makeClient(String clientName, String accountNumber) {
        Client client = new Client();
        client.setType(new ClientType(2l));
        client.setContractStart(LocalDate.now());
        client.setContractEnd(LocalDate.now().plusYears(1));
        client.setName(clientName);
        client.setContractOwner(new UserAdmin(1l, 0));
        client.setSalesForceAccount(new SalesForce(accountNumber));
        return client;
    }
}
