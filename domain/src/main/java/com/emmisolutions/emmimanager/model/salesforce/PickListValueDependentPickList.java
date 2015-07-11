package com.emmisolutions.emmimanager.model.salesforce;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Sometimes selecting one value enables more pick lists
 */
public class PickListValueDependentPickList extends PickListValue {

    @XmlElement(name = "requiredWhenChosen")
    @XmlElementWrapper(name = "requiredWhenChosen")
    private List<PickListCaseField> requiredWhenChosen;

    public List<PickListCaseField> getRequiredWhenChosen() {
        return requiredWhenChosen;
    }

    public void setRequiredWhenChosen(List<PickListCaseField> requiredWhenChosen) {
        this.requiredWhenChosen = requiredWhenChosen;
    }

    public void addRequiredWhenChosen(PickListCaseField toAdd) {
        if (requiredWhenChosen == null) {
            requiredWhenChosen = new ArrayList<>();
        }
        requiredWhenChosen.add(toAdd);
    }

    @Override
    public String toString() {
        return "{" +
                "\"value\":\"" + getValue() + "\"" +
                ", \"requiredWhenChosen\":" + requiredWhenChosen +
                '}';
    }

}
