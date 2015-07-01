package com.emmisolutions.emmimanager.model.salesforce;

/**
 * Field that has a String value
 */
public class StringCaseField extends CaseField {

    private String value;

    private Integer maxLength;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String toString() {
        return "{" +
                "\"class\": \"" + getClass().getSimpleName() + "\"" +
                ", \"type\":\"" + getType() + "\"" +
                ", \"name\":\"" + getName() + "\"" +
                ", \"label\":\"" + getLabel() + "\"" +
                ", \"required\":" + isRequired() +
                ", \"maxLength\":" + maxLength +
                ", \"value\":\"" + value + "\"" +
                '}';
    }
}
