package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.SalesForceSearchResponse;

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
}
