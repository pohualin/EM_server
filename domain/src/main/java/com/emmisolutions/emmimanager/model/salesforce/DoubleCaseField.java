package com.emmisolutions.emmimanager.model.salesforce;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.DOUBLE;

/**
 * A field that contains a double value
 */
public class DoubleCaseField extends CaseField {

    private Double value;

    private int precision;

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
    public FieldType getType() {
        return DOUBLE;
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
