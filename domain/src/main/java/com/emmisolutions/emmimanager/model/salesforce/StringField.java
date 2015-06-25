package com.emmisolutions.emmimanager.model.salesforce;

/**
 * Field that has a String value
 */
public class StringField extends Field {

    private String value;

    private int maxLength;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
