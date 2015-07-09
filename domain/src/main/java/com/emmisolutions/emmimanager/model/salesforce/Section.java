package com.emmisolutions.emmimanager.model.salesforce;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * A grouping of fields
 */
@XmlRootElement(name = "section")
public class Section {

    private String name;

    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "fields")
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
                "\"name\":\"" + name + "\"" +
                ", \"fields\":" + caseFields +
                '}';
    }
}
