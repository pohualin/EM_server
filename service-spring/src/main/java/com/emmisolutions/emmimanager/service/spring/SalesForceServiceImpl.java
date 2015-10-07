package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.model.TeamSalesForce;
import com.emmisolutions.emmimanager.model.salesforce.*;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.PatientPersistence;
import com.emmisolutions.emmimanager.persistence.SalesForcePersistence;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.salesforce.wsc.CaseManager;
import com.emmisolutions.emmimanager.salesforce.wsc.SalesForceLookup;
import com.emmisolutions.emmimanager.service.SalesForceService;
import com.emmisolutions.emmimanager.service.ScheduleService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

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
    UserClientPersistence userClientPersistence;

    @Resource
    PatientPersistence patientPersistence;

    @Resource
    ScheduleService scheduleService;

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
    public SalesForce findAccountById(String searchString) {
        return salesForceLookup.findAccountById(searchString);
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
        Set<IdNameLookupResult> ret = new LinkedHashSet<>();
        UserClient fromDb = userClientPersistence.reload(userClient);
        if (fromDb != null) {
            SalesForce salesForce = fromDb.getClient().getSalesForceAccount();
            IdNameLookupResult clientResult = new IdNameLookupResult(salesForce.getAccountNumber(), salesForce.getName());
            clientResult.setClient(true);
            ret.add(clientResult);

            // a little bit hacky but we need to reload via login to ensure the team roles have been loaded
            for (UserClientUserClientTeamRole teamRole :
                    userClientPersistence.fetchUserWillFullPermissions(fromDb.getLogin()).getTeamRoles()) {
                TeamSalesForce teamSalesForce = teamRole.getTeam().getSalesForceAccount();
                ret.add(new IdNameLookupResult(teamSalesForce.getAccountNumber(), teamSalesForce.getName()));
            }
        }
        return new ArrayList<>(ret);
    }

    /**
     * Adds client patient is associated to, and any teams where that patient has been issued a program
     *
     * @param patient to find accounts for
     * @return sf accounts
     */
    @Override
    public List<IdNameLookupResult> possibleAccounts(Patient patient) {
        Set<IdNameLookupResult> ret = new LinkedHashSet<>();

        Patient fromDb = patientPersistence.reload(patient);
        if (fromDb != null) {
            SalesForce salesForce = fromDb.getClient().getSalesForceAccount();
            IdNameLookupResult clientResult = new IdNameLookupResult(salesForce.getAccountNumber(), salesForce.getName());
            clientResult.setClient(true);
            ret.add(clientResult);

            Pageable pageable = new PageRequest(0, 50);
            Page<ScheduledProgram> programPage;
            do {
                programPage = scheduleService.findAllByPatient(fromDb, pageable);
                if (programPage != null) {
                    for (ScheduledProgram scheduledProgram : programPage) {
                        TeamSalesForce teamSalesForce = scheduledProgram.getTeam().getSalesForceAccount();
                        ret.add(new IdNameLookupResult(teamSalesForce.getAccountNumber(), teamSalesForce.getName()));
                    }
                    pageable = pageable.next();
                }
            } while (programPage != null && programPage.hasNext());
        }
        return new ArrayList<>(ret);
    }
}
