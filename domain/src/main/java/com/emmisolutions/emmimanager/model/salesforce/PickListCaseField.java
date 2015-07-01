package com.emmisolutions.emmimanager.model.salesforce;

import java.util.List;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.MULTI_PICK_LIST;
import static com.emmisolutions.emmimanager.model.salesforce.FieldType.PICK_LIST;

/**
 * For select fields.. single and multi
 */
public class PickListCaseField extends CaseField {

    private List<String> values;

    private List<DependentPickListPossibleValue> options;

    private boolean multiSelect;

    @Override
    public FieldType getType() {
        return !multiSelect ? PICK_LIST : MULTI_PICK_LIST;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<DependentPickListPossibleValue> getOptions() {
        return options;
    }

    public void setOptions(List<DependentPickListPossibleValue> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "{" +
                "\"class\": \"" + getClass().getSimpleName() + "\"" +
                ", \"type\":\"" + getType() + "\"" +
                ", \"name\":\"" + getName() + "\"" +
                ", \"label\":\"" + getLabel() + "\"" +
                ", \"required\":" + isRequired() +
                ", \"multiSelect\":" + multiSelect +
                ", \"options\":" + options +
                ", \"values\":" + toJsonString(values) +
                '}';
    }

    private String toJsonString(List toPrint) {
        if (toPrint == null) {
            return "null";
        }
        Object[] a = toPrint.toArray();

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append("[");
        for (int i = 0; ; i++) {
            b.append("\"");
            b.append(String.valueOf(a[i]));
            b.append("\"");
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
