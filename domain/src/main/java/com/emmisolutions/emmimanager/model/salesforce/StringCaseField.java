package com.emmisolutions.emmimanager.model.salesforce;

import javax.xml.bind.annotation.XmlRootElement;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.STRING;

/**
 * Field that has a String value
 */
@XmlRootElement(name = "string-case-field")
public class StringCaseField extends CaseField {

    private String value = "";
    private Integer maxLength;

    public StringCaseField() {
        setType(STRING);
    }

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
                "\"type\":\"" + getType() + "\"" +
                ", \"name\":\"" + getName() + "\"" +
                ", \"label\":\"" + getLabel() + "\"" +
                ", \"required\":" + isRequired() +
                ", \"maxLength\":" + maxLength +
                ", \"value\":\"" + value + "\"" +
                '}';
    }
}
