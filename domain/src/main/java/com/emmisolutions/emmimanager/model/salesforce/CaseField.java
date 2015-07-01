package com.emmisolutions.emmimanager.model.salesforce;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * A Field on a salesforce form
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BooleanCaseField.class, name = "BOOLEAN"),
        @JsonSubTypes.Type(value = DateCaseField.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeCaseField.class, name = "DATETIME"),
        @JsonSubTypes.Type(value = DoubleCaseField.class, name = "DOUBLE"),
        @JsonSubTypes.Type(value = PickListCaseField.class, name = "PICK_LIST"),
        @JsonSubTypes.Type(value = PickListCaseField.class, name = "MULTI_PICK_LIST"),
        @JsonSubTypes.Type(value = ReferenceCaseField.class, name = "REFERENCE"),
        @JsonSubTypes.Type(value = StringCaseField.class, name = "STRING"),
        @JsonSubTypes.Type(value = StringCaseField.class, name = "EMAIL"),
        @JsonSubTypes.Type(value = StringCaseField.class, name = "PHONE"),
        @JsonSubTypes.Type(value = StringCaseField.class, name = "TEXTAREA")
})
public abstract class CaseField {

    private String label;
    private String name;
    private FieldType type;
    private boolean required;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public String toString() {
        return "{" +
                "\"class\": \"" + getClass().getSimpleName() + "\"" +
                ", \"name\":\"" + name + "\"" +
                ", \"label\":\"" + label + "\"" +
                ", \"type\":\"" + getType() + "\"" +
                ", \"required\":" + required +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
