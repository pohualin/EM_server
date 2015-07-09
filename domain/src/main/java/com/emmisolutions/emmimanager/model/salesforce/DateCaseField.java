package com.emmisolutions.emmimanager.model.salesforce;

import org.joda.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.DATE;

/**
 * A field that contains a double value
 */
@XmlRootElement(name = "date-case-field")
public class DateCaseField extends CaseField {

    private LocalDate value;

    public DateCaseField() {
        setType(DATE);
    }

    public LocalDate getValue() {
        return value;
    }

    public void setValue(LocalDate value) {
        this.value = value;
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
