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

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.PICK_LIST;

/**
 * For select fields.. single and multi
 */
@XmlRootElement(name = "picklist-case-field")
public class PickListCaseField extends CaseField implements PickList {

    private PickListValue value = new PickListValue();

    @XmlElement(name = "options")
    @XmlElementWrapper(name = "options")
    private List<PickListValueDependentPickList> options;

    public PickListCaseField() {
        setType(PICK_LIST);
    }

    public PickListValue getValue() {
        return value;
    }

    public void setValue(PickListValue value) {
        this.value = value;
    }

    @Override
    @XmlTransient
    public void setValues(List<PickListValue> values) {
        if (!CollectionUtils.isEmpty(values)) {
            setValue(values.get(0));
        }
    }

    public List<PickListValueDependentPickList> getOptions() {
        return options;
    }

    public void setOptions(List<PickListValueDependentPickList> options) {
        this.options = options;
    }

    @Override
    @XmlTransient
    public List<PickListValueDependentPickList> getSelectedOptions() {
        List<PickListValueDependentPickList> ret = new ArrayList<>();
        if (value != null && !CollectionUtils.isEmpty(options)) {
            Map<String, PickListValueDependentPickList> stringOptionMap = new HashMap<>();
            for (PickListValueDependentPickList option : options) {
                stringOptionMap.put(option.getValue(), option);
            }
            PickListValueDependentPickList dependentPickListPossibleValue = stringOptionMap.get(value.getValue());
            if (dependentPickListPossibleValue != null) {
                ret.add(dependentPickListPossibleValue);
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
                ", \"value\":" + value +
                '}';
    }
}
