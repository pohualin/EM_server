package com.emmisolutions.emmimanager.model.salesforce;

import java.util.ArrayList;
import java.util.List;

/**
 * A grouping of fields
 */
public class Section {

    private String name;
    private List<CaseField> caseFields = new ArrayList<>();

    public List<CaseField> getCaseFields() {
        return caseFields;
    }

    public void setCaseFields(List<CaseField> caseFields) {
        this.caseFields = caseFields;
    }

    public void addField(CaseField caseField) {
        caseFields.add(caseField);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "\"class\": \"" + getClass().getSimpleName() + "\"" +
                ", \"name\":\"" + name + "\"" +
                ", \"fields\":" + caseFields +
                '}';
    }
}
