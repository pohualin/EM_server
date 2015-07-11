package com.emmisolutions.emmimanager.model.salesforce;

import org.joda.time.LocalDateTime;

import javax.xml.bind.annotation.XmlRootElement;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.DATETIME;

/**
 * A field that contains a double value
 */
@XmlRootElement(name = "datetime-case-field")
public class DateTimeCaseField extends CaseField {

    private LocalDateTime value;

    public DateTimeCaseField() {
        super.setType(DATETIME);
    }

    public LocalDateTime getValue() {
        return value;
    }

    public void setValue(LocalDateTime value) {
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
