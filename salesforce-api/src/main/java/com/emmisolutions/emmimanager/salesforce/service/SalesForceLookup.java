package com.emmisolutions.emmimanager.salesforce.service;

import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;

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
    SalesForceSearchResponse findAccountsByNameOrId(String searchString);

    /**
     * Queries SalesForce for accounts
     *
     * @param searchString the filter
     * @param pageSize    the number of objects to request from salesforce
     * @return a search response
     */
    SalesForceSearchResponse findAccountsByNameOrId(String searchString, int pageSize);
}
