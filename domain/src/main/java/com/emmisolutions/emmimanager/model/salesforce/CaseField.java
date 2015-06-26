package com.emmisolutions.emmimanager.model.salesforce;

/**
 * A Field on a salesforce form
 */
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
