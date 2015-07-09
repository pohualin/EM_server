package com.emmisolutions.emmimanager.model.salesforce;

import java.util.List;

/**
 * For dependent pick list common functions
 */
public interface PickList {

    List<PickListValueDependentPickList> getSelectedOptions();

    void setLabel(String label);

    void setOptions(List<PickListValueDependentPickList> options);

    void setValues(List<PickListValue> values);
}
