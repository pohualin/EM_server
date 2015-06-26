package com.emmisolutions.emmimanager.model.salesforce;

import java.util.List;

/**
 * A salesforce case
 */
public class CaseForm {

    private CaseType type;
    private List<Section> sections;

    public CaseType getType() {
        return type;
    }

    public void setType(CaseType type) {
        this.type = type;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    @Override
    public String toString() {
        return "CaseForm{" +
                "type=" + type +
                ", sections=" + sections +
                '}';
    }
}
