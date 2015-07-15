package com.emmisolutions.emmimanager.web.rest.admin.model.case_management;

import com.emmisolutions.emmimanager.model.salesforce.IdNameLookupResult;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Case management reference data
 */
@XmlRootElement(name = "case-management-reference-data")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReferenceData {

    @XmlElement(name = "type")
    @XmlElementWrapper(name = "types")
    List<CaseTypeResource> caseTypes;

    @XmlElement(name = "account")
    @XmlElementWrapper(name = "accounts")
    List<IdNameLookupResult> accounts;

    public ReferenceData(List<CaseTypeResource> caseTypeResources, List<IdNameLookupResult> accounts) {
        this.caseTypes = caseTypeResources;
        this.accounts = accounts;
    }

}
