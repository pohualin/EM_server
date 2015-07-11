package com.emmisolutions.emmimanager.model.salesforce;

import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * The possible types of salesforce fields
 */
public enum FieldType {
    BOOLEAN("boolean"), STRING("string"), EMAIL("email"), PICK_LIST("picklist"), MULTI_PICK_LIST("multipicklist"),
    TEXTAREA("textarea"), DATETIME("datetime"), PHONE("phone"), DATE("date"), DOUBLE("double"),
    REFERENCE("reference");

    private static Map<String, FieldType> valuesToEnums;

    static {
        valuesToEnums = new HashMap<>();
        for (FieldType fieldType : EnumSet.allOf(FieldType.class)) {
            for (String sfValue : fieldType.sfValues) {
                valuesToEnums.put(sfValue, fieldType);
            }
        }
    }

    private String[] sfValues;

    FieldType(String... salesForceType) {
        this.sfValues = salesForceType;
    }

    /**
     * Finds a FieldType from the salesforce type.
     *
     * @param string the salesforce type
     * @return the FieldType or null if there is no match
     */
    public static FieldType fromSalesForceTypeString(String string) {
        if (StringUtils.isNotBlank(string)) {
            return valuesToEnums.get(StringUtils.lowerCase(string));
        }
        return null;
    }
}
