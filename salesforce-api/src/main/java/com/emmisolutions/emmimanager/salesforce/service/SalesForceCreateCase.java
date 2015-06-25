package com.emmisolutions.emmimanager.salesforce.service;

import com.emmisolutions.emmimanager.model.SalesForce;

/**
 * Creates cases within SF
 */
public interface SalesForceCreateCase {

    /**
     * Opens a SF case
     */
    void openCase(SalesForce salesForceAccount);
}
