package com.emmisolutions.emmimanager.salesforce.service;

import com.emmisolutions.emmimanager.salesforce.model.AccountSearchResponse;

/**
 * Interactions with SalesForce are defined here
 */
public interface SalesForce {

    /**
     * Queries SalesForce for accounts using a default page size and a filter
     *
     * @param accountName the filter
     * @return a search response
     */
    AccountSearchResponse findAccountsByName(String accountName);

    /**
     * Queries SalesForce for accounts
     *
     * @param accountName the filter
     * @param pageSize    the number of objects to request from salesforce
     * @return a search response
     */
    AccountSearchResponse findAccountsByName(String accountName, int pageSize);
}
