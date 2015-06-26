package com.emmisolutions.emmimanager.model.salesforce;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.MULTI_PICK_LIST;
import static com.emmisolutions.emmimanager.model.salesforce.FieldType.PICK_LIST;

/**
 * For select fields.. single and multi
 */
public class PickListCaseField extends CaseField {

    private String[] values;

    private String[] options;

    private boolean multiSelect;

    @Override
    public FieldType getType() {
        return !multiSelect ? PICK_LIST : MULTI_PICK_LIST;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
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
                ", \"options\":" + toJsonString(options) +
                ", \"values\":" + toJsonString(values) +
                '}';
    }

    private String toJsonString(Object[] a) {
        if (a == null)
            return "null";

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
