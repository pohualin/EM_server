package com.emmisolutions.emmimanager.model.salesforce;

import org.springframework.util.CollectionUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.MULTI_PICK_LIST;

/**
 * For multi-select fields..
 */
@XmlRootElement(name = "multi-picklist-case-field")
public class MultiPickListCaseField extends CaseField implements PickList {

    @XmlElement(name = "values")
    @XmlElementWrapper(name = "values")
    private List<PickListValue> values;

    @XmlElement(name = "options")
    @XmlElementWrapper(name = "options")
    private List<PickListValueDependentPickList> options;

    public MultiPickListCaseField() {
        setType(MULTI_PICK_LIST);
    }

    public List<PickListValue> getValues() {
        return values;
    }

    @Override
    public void setValues(List<PickListValue> values) {
        this.values = values;
    }

    public List<PickListValueDependentPickList> getOptions() {
        return options;
    }

    @Override
    public void setOptions(List<PickListValueDependentPickList> options) {
        this.options = options;
    }

    @Override
    @XmlTransient
    public List<PickListValueDependentPickList> getSelectedOptions() {
        List<PickListValueDependentPickList> ret = new ArrayList<>();
        if (!CollectionUtils.isEmpty(values) && !CollectionUtils.isEmpty(options)) {
            Map<String, PickListValueDependentPickList> stringOptionMap = new HashMap<>();
            for (PickListValueDependentPickList option : options) {
                stringOptionMap.put(option.getValue(), option);
            }
            for (PickListValue value : values) {
                PickListValueDependentPickList dependentPickListPossibleValue = stringOptionMap.get(value.getValue());
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
                ", \"options\":" + options +
                ", \"values\":" + toJsonString(values) +
                '}';
    }
}
