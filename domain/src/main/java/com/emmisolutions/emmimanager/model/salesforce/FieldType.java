package com.emmisolutions.emmimanager.model.salesforce;

import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * The possible types of salesforce fields
 */
public enum FieldType {
    BOOLEAN("boolean"), STRING("string"), EMAIL("email"), PICK_LIST("picklist"),
    TEXTAREA("textarea"), DATETIME("datetime"), PHONE("phone"), DATE("date"), DOUBLE("double"),
    MULTI_PICK_LIST("multipicklist"), REFERENCE("reference");

    private static Map<String, FieldType> valuesToEnums;

    static {
        valuesToEnums = new HashMap<>();
        for (FieldType fieldType : EnumSet.allOf(FieldType.class)) {
            valuesToEnums.put(fieldType.sfValue, fieldType);
        }
    }

    private String sfValue;

    FieldType(String salesForceType) {
        this.sfValue = salesForceType;
    }

    public static FieldType fromString(String string) {
        if (StringUtils.isNotBlank(string)) {
            return valuesToEnums.get(StringUtils.lowerCase(string));
        }
        return null;
    }
}
