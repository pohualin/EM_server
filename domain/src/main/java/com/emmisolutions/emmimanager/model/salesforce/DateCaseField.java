package com.emmisolutions.emmimanager.model.salesforce;

import org.joda.time.LocalDate;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.DATE;

/**
 * A field that contains a double value
 */
public class DateCaseField extends CaseField {

    private LocalDate value;

    public LocalDate getValue() {
        return value;
    }

    public void setValue(LocalDate value) {
        this.value = value;
    }

    @Override
    public FieldType getType() {
        return DATE;
    }

    @Override
    public String toString() {
        return "{" +
                "\"class\": \"" + getClass().getSimpleName() + "\"" +
                ", \"type\":\"" + getType() + "\"" +
                ", \"name\":\"" + getName() + "\"" +
                ", \"label\":\"" + getLabel() + "\"" +
                ", \"required\":" + isRequired() +
                ", \"value\":" + value +
                '}';
    }
}
