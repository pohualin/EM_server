package com.emmisolutions.emmimanager.web.rest.admin.model.case_management;

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

    public ReferenceData(List<CaseTypeResource> caseTypeResources) {
        this.caseTypes = caseTypeResources;
    }

}
