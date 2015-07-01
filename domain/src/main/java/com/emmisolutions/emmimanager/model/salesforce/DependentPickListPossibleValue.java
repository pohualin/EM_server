package com.emmisolutions.emmimanager.model.salesforce;

import java.util.ArrayList;
import java.util.List;

/**
 * Sometimes selecting one value enables more pick lists
 */
public class DependentPickListPossibleValue {

    private String value;
    private List<PickListCaseField> requiredWhenChosen = new ArrayList<>();

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<PickListCaseField> getRequiredWhenChosen() {
        return requiredWhenChosen;
    }

    public void setRequiredWhenChosen(List<PickListCaseField> requiredWhenChosen) {
        this.requiredWhenChosen = requiredWhenChosen;
    }

    @Override
    public String toString() {
        return "{" +
                "\"value\":\"" + value + "\"" +
                ", \"requiredWhenChosen\":" + requiredWhenChosen +
                '}';
    }

}
