package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.SalesForce;

/**
 * SalesForce service
 */
public interface SalesForceService {

    /**
     * Creates a new SalesForce object
     *
     * @param salesForce to be created
     * @return the saved object
     */
    SalesForce create(SalesForce salesForce);

    /**
     * Update the SalesForce object
     *
     * @param salesForce to be updated
     * @return the updated object
     */
    SalesForce update(SalesForce salesForce);

    /**
     * Fetches a SalesForce from persistent storage
     * @param salesForce to be fetched
     * @return the object or null
     */
    SalesForce reload(SalesForce salesForce);
}
