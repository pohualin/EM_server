package com.emmisolutions.emmimanager.model.salesforce;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

/**
 * A Case (SalesForce) field on a form
 */
@XmlSeeAlso({BooleanCaseField.class, DateCaseField.class, DateTimeCaseField.class,
        DoubleCaseField.class, PickListCaseField.class, ReferenceCaseField.class,
        StringCaseField.class})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BooleanCaseField.class, name = "BOOLEAN"),
        @JsonSubTypes.Type(value = DateCaseField.class, name = "DATE"),
        @JsonSubTypes.Type(value = DateTimeCaseField.class, name = "DATETIME"),
        @JsonSubTypes.Type(value = DoubleCaseField.class, name = "DOUBLE"),
        @JsonSubTypes.Type(value = PickListCaseField.class, name = "PICK_LIST"),
        @JsonSubTypes.Type(value = MultiPickListCaseField.class, name = "MULTI_PICK_LIST"),
        @JsonSubTypes.Type(value = ReferenceCaseField.class, name = "REFERENCE"),
        @JsonSubTypes.Type(value = StringCaseField.class, name = "STRING"),
        @JsonSubTypes.Type(value = EmailCaseField.class, name = "EMAIL"),
        @JsonSubTypes.Type(value = PhoneCaseField.class, name = "PHONE"),
        @JsonSubTypes.Type(value = TextAreaCaseField.class, name = "TEXTAREA")
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "\"type\":\"" + getType() + "\"" +
                ", \"name\":\"" + name + "\"" +
                ", \"label\":\"" + label + "\"" +
                ", \"required\":" + required +
                '}';
    }

    protected String toJsonString(List toPrint) {
        if (toPrint == null) {
            return "null";
        }
        Object[] a = toPrint.toArray();

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append("[");
        for (int i = 0; ; i++) {
            b.append("\"");
            b.append(String.valueOf(a[i]));
            b.append("\"");
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
