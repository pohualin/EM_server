package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.SalesForce;
import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.persistence.SalesForcePersistence;
import com.emmisolutions.emmimanager.salesforce.service.SalesForceLookup;
import com.emmisolutions.emmimanager.service.SalesForceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

}
