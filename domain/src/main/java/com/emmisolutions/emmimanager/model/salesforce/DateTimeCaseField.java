package com.emmisolutions.emmimanager.model.salesforce;

import org.joda.time.LocalDateTime;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.DATETIME;

/**
 * A field that contains a double value
 */
public class DateTimeCaseField extends CaseField {

    private LocalDateTime value;

    public LocalDateTime getValue() {
        return value;
    }

    public void setValue(LocalDateTime value) {
        this.value = value;
    }

    @Override
    public FieldType getType() {
        return DATETIME;
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
