package com.emmisolutions.emmimanager.salesforce.service;

import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;
import com.emmisolutions.emmimanager.model.salesforce.IdNameLookupResultContainer;

/**
 * Interactions with SalesForce are defined here
 */
public interface SalesForceLookup {

    /**
     * Queries SalesForce for accounts using a default page size and a filter
     *
     * @param searchString the filter
     * @return a search response
     */
    SalesForceSearchResponse findAccounts(String searchString);

    /**
     * Queries SalesForce for accounts
     *
     * @param searchString the filter
     * @param pageSize     the number of objects to request from salesforce
     * @return a search response
     */
    SalesForceSearchResponse findAccounts(String searchString, int pageSize);


    /**
     * Queries SalesForce for types
     *
     * @param searchString to find within each type
     * @param pageSize     the total number to return
     * @param types        to search across
     * @return a result container
     */
    IdNameLookupResultContainer find(String searchString, int pageSize, String... types);
}
