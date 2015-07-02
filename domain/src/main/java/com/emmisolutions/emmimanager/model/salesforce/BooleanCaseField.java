package com.emmisolutions.emmimanager.model.salesforce;

import java.util.ArrayList;
import java.util.List;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.BOOLEAN;

/**
 * A boolean field.
 */
public class BooleanCaseField extends CaseField {

    private Boolean value = Boolean.FALSE;

    private List<PickListCaseField> requiredPicklistsWhenTrue = new ArrayList<>();

    private List<PickListCaseField> requiredPicklistsWhenFalse = new ArrayList<>();

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public FieldType getType() {
        return BOOLEAN;
    }

    public List<PickListCaseField> getRequiredPicklistsWhenTrue() {
        return requiredPicklistsWhenTrue;
    }

    public void setRequiredPicklistsWhenTrue(List<PickListCaseField> requiredPicklistsWhenTrue) {
        this.requiredPicklistsWhenTrue = requiredPicklistsWhenTrue;
    }

    public List<PickListCaseField> getRequiredPicklistsWhenFalse() {
        return requiredPicklistsWhenFalse;
    }

    public void setRequiredPicklistsWhenFalse(List<PickListCaseField> requiredPicklistsWhenFalse) {
        this.requiredPicklistsWhenFalse = requiredPicklistsWhenFalse;
    }

    @Override
    public String toString() {
        return "{" +
                "\"type\":\"" + getType() + "\"" +
                ", \"name\":\"" + getName() + "\"" +
                ", \"label\":\"" + getLabel() + "\"" +
                ", \"required\":" + isRequired() +
                ", \"value\":" + value +
                '}';
    }
}
