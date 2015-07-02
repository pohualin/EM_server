package com.emmisolutions.emmimanager.model.salesforce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.REFERENCE;

/**
 * A reference field. This is a reference to a different object type.
 */
public class ReferenceCaseField extends CaseField {

    private String referenceId = "";

    private String referenceName = "";

    private List<String> referenceTypes;

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    @Override
    public FieldType getType() {
        return REFERENCE;
    }

    public List<String> getReferenceTypes() {
        return referenceTypes;
    }

    public void setReferenceTypes(List<String> referenceTypes) {
        this.referenceTypes = referenceTypes;
    }

    public void addReferenceTypes(String... referenceTypes) {
        if (this.referenceTypes == null) {
            this.referenceTypes = new ArrayList<>();
        }
        Collections.addAll(this.referenceTypes, referenceTypes);
    }

    @Override
    public String toString() {
        return "{" +
                "\"type\":\"" + getType() + "\"" +
                ", \"name\":\"" + getName() + "\"" +
                ", \"label\":\"" + getLabel() + '"' +
                ", \"required\":" + isRequired() +
                ", \"referenceId\":\"" + referenceId + "\"" +
                ", \"referenceName\":\"" + referenceName + "\"" +
                ", \"referenceTypes\":" + toJsonString(referenceTypes) +
                '}';
    }
}
