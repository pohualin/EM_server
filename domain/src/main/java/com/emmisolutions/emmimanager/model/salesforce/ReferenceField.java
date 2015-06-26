package com.emmisolutions.emmimanager.model.salesforce;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.REFERENCE;

/**
 * A reference field. This is a reference to a different object type.
 */
public class ReferenceField extends Field {

    private String referenceId;

    private String referenceName;

    private String referenceType;

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

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    @Override
    public FieldType getType() {
        return REFERENCE;
    }

    @Override
    public String toString() {
        return "ReferenceField{" +
                "label='" + getLabel() + '\'' +
                ", required=" + isRequired() +
                ", referenceId='" + referenceId + '\'' +
                ", referenceName='" + referenceName + '\'' +
                ", referenceType='" + referenceType + '\'' +
                '}';
    }
}
