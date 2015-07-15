package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.model.TeamSalesForce;
import com.emmisolutions.emmimanager.model.salesforce.*;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.SalesForcePersistence;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.salesforce.wsc.CaseManager;
import com.emmisolutions.emmimanager.salesforce.wsc.SalesForceLookup;
import com.emmisolutions.emmimanager.service.SalesForceService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SalesForce Service Implementation
 */
@Service
public class SalesForceServiceImpl implements SalesForceService {

    @Resource
    SalesForcePersistence salesForcePersistence;

    @Resource
    SalesForceLookup salesForceLookup;

    @Resource(name = "adminUserDetailsService")
    UserDetailsService userDetailsService;

    @Resource
    UserClientPersistence userClientService;

    @Resource
    CaseManager caseManager;

    @Override
    @Transactional(readOnly = true)
    public SalesForceSearchResponse find(String searchString) {
        SalesForceSearchResponse salesForceSearchResponse = salesForceLookup.findAccounts(searchString, 20);
        Set<String> accountNumbersToMatch = new HashSet<>();
        for (SalesForce salesForce : salesForceSearchResponse.getAccounts()) {
            accountNumbersToMatch.add(salesForce.getAccountNumber());
        }
        List<SalesForce> persistentAccounts = salesForcePersistence.findByAccountNumbers(accountNumbersToMatch);
        // update the id, version and client on accounts found within the search response
        for (SalesForce persistentAccount : persistentAccounts) {
            for (SalesForce salesForce : salesForceSearchResponse.getAccounts()) {
                if (persistentAccount.getAccountNumber().equals(salesForce.getAccountNumber())) {
                    salesForce.setId(persistentAccount.getId());
                    salesForce.setVersion(persistentAccount.getVersion());
                    salesForce.setClient(persistentAccount.getClient());
                }
            }
        }
        return salesForceSearchResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public SalesForceSearchResponse findForTeam(String searchString) {
        return salesForceLookup.findAccounts(searchString, 20);
    }

    @Override
    public List<CaseType> possibleCaseTypes() {
        return caseManager.caseTypes();
    }

    @Override
    public CaseForm blankFormFor(CaseType caseType) {
        return caseManager.newCase(caseType);
    }

    @Override
    public CaseSaveResult saveCase(CaseForm caseForm) {
        return caseManager.saveCase(caseForm, (UserAdmin) userDetailsService.getLoggedInUser());
    }

    @Override
    public IdNameLookupResultContainer findByNameInTypes(String searchString, Integer pageSize, String... types) {
        return salesForceLookup.find(searchString, pageSize != null ? pageSize : 50, types);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IdNameLookupResult> possibleAccounts(UserClient userClient) {
        List<IdNameLookupResult> ret = new ArrayList<>();
        UserClient fromDb = userClientService.reload(userClient);
        if (fromDb != null) {
            SalesForce salesForce = fromDb.getClient().getSalesForceAccount();
            ret.add(new IdNameLookupResult(salesForce.getAccountNumber(), salesForce.getName()));

            // a little bit hacky but we need to reload via login to ensure the team roles have been loaded
            for (UserClientUserClientTeamRole teamRole :
                    userClientService.fetchUserWillFullPermissions(fromDb.getLogin()).getTeamRoles()) {
                TeamSalesForce teamSalesForce = teamRole.getTeam().getSalesForceAccount();
                ret.add(new IdNameLookupResult(teamSalesForce.getAccountNumber(), teamSalesForce.getName()));
            }
        }
        return ret;
    }
}
