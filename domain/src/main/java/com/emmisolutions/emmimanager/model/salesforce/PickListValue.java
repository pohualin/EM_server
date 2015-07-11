package com.emmisolutions.emmimanager.model.salesforce;

/**
 * A pick list value
 */
public class PickListValue {

    private String value;

    public PickListValue() {
    }

    public PickListValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" +
                "\"value\":\"" + value + "\"" +
                '}';
    }
}
