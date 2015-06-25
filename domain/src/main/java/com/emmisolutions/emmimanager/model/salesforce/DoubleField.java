package com.emmisolutions.emmimanager.model.salesforce;

/**
 * A field that contains a double value
 */
public class DoubleField extends Field {

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
}
