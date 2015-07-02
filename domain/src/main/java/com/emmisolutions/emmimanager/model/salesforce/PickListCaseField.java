package com.emmisolutions.emmimanager.model.salesforce;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return PICK_LIST;
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

    public List<DependentPickListPossibleValue> getSelectedOptions() {
        List<DependentPickListPossibleValue> ret = new ArrayList<>();
        if (!CollectionUtils.isEmpty(values) && !CollectionUtils.isEmpty(options)) {
            Map<String, DependentPickListPossibleValue> stringOptionMap = new HashMap<>();
            for (DependentPickListPossibleValue option : options) {
                stringOptionMap.put(option.getValue(), option);
            }
            for (String value : values) {
                DependentPickListPossibleValue dependentPickListPossibleValue = stringOptionMap.get(value);
                if (dependentPickListPossibleValue != null) {
                    ret.add(dependentPickListPossibleValue);
                }
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return "{" +
                "\"type\":\"" + getType() + "\"" +
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
