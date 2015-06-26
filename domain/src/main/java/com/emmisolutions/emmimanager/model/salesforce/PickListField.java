package com.emmisolutions.emmimanager.model.salesforce;

import java.util.Arrays;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.MULTI_PICK_LIST;
import static com.emmisolutions.emmimanager.model.salesforce.FieldType.PICK_LIST;

/**
 * For select fields.. single and multi
 */
public class PickListField extends Field {

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
        return "PickListField{" +
                "label='" + getLabel() + '\'' +
                ", required=" + isRequired() +
                ", multiSelect=" + multiSelect +
                ", options=" + Arrays.toString(options) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
