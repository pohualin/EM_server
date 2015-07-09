package com.emmisolutions.emmimanager.model.salesforce;

import javax.xml.bind.annotation.XmlRootElement;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.DOUBLE;

/**
 * A field that contains a double value
 */
@XmlRootElement(name = "double-case-field")
public class DoubleCaseField extends CaseField {

    private Double value;

    private int precision;

    public DoubleCaseField() {
        setType(DOUBLE);
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    @Override
    public String toString() {
        return "{" +
                "\"type\":\"" + getType() + "\"" +
                ", \"name\":\"" + getName() + "\"" +
                ", \"label\":\"" + getLabel() + "\"" +
                ", \"required\":" + isRequired() +
                ", \"precision\":" + precision +
                ", \"value\":" + value +
                '}';
    }
}
