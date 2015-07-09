package com.emmisolutions.emmimanager.model.salesforce;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.BOOLEAN;

/**
 * A boolean field.
 */
@XmlRootElement(name = "boolean-case-field")
public class BooleanCaseField extends CaseField {

    private Boolean value = Boolean.FALSE;

    @XmlElement(name = "requiredPicklistsWhenTrue")
    @XmlElementWrapper(name = "requiredPicklistsWhenTrue")
    private List<PickListCaseField> requiredPicklistsWhenTrue = new ArrayList<>();

    @XmlElement(name = "requiredPicklistsWhenFalse")
    @XmlElementWrapper(name = "requiredPicklistsWhenFalse")
    private List<PickListCaseField> requiredPicklistsWhenFalse = new ArrayList<>();

    public BooleanCaseField() {
        setType(BOOLEAN);
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
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
