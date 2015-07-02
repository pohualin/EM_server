package com.emmisolutions.emmimanager.salesforce.service;

import com.emmisolutions.emmimanager.model.salesforce.CaseForm;
import com.emmisolutions.emmimanager.model.salesforce.CaseSaveResult;
import com.emmisolutions.emmimanager.model.salesforce.CaseType;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;

import java.util.List;

/**
 * Creates cases within SF
 */
public interface CaseManager {

    /**
     * Opens a SF case
     */
    CaseSaveResult saveCase(CaseForm caseForm, UserAdmin user);

    /**
     * Creates a new CaseForm for a particular CaseType
     *
     * @param caseType to retrieve the form
     * @return a blank form or null
     */
    CaseForm newCase(CaseType caseType);

    /**
     * All possible case types
     *
     * @return List of case types
     */
    List<CaseType> caseTypes();
}
