package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.model.salesforce.*;
import com.emmisolutions.emmimanager.model.user.client.UserClient;

import java.util.List;

/**
 * SalesForce service
 */
public interface SalesForceService {

    /**
     * Searches SalesForce for accounts containing the search string. This
     * method will set the id and version for all accounts which are already
     * bound to a client.
     *
     * @param searchString to find
     * @return a SalesForceSearchResponse
     */
    SalesForceSearchResponse find(String searchString);

    /**
     * Searches SalesForce for accounts containing the search string.
     *
     * @param searchString to find
     * @return a SalesForceSearchResponse
     */
    SalesForceSearchResponse findForTeam(String searchString);

    /**
     * Find all salesforce case types
     *
     * @return a list of CaseType objects
     */
    List<CaseType> possibleCaseTypes();

    /**
     * Retrieve a blank for a particular case type
     *
     * @param caseType for the form
     * @return a CaseForm
     */
    CaseForm blankFormFor(CaseType caseType);

    /**
     * Saves a case form
     *
     * @param caseForm to save
     * @return the save results
     */
    CaseSaveResult saveCase(CaseForm caseForm);

    /**
     * Performs a salesforce search for the name across the passed types
     *
     * @param searchString to find
     * @param pageSize     number to look for
     * @param types        to search over (these are salesforce types)
     * @return the results
     */
    IdNameLookupResultContainer findByNameInTypes(String searchString, Integer pageSize, String... types);

    /**
     * Finds possible salesforce accounts for a user client.
     *
     * @param userClient to find the accounts
     * @return list of IdNameLookupResult
     */
    List<IdNameLookupResult> possibleAccounts(UserClient userClient);

}
