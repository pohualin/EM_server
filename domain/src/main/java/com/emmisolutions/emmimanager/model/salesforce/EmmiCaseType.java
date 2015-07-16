package com.emmisolutions.emmimanager.model.salesforce;

import org.apache.commons.lang3.StringUtils;

/**
 * Emmi Case types match with SF program types by id
 */
public enum EmmiCaseType {
    CLIENT("012500000009F6LAAU"), PATIENT("012500000009F6GAAU"),
    ADVISOR("012500000009G9aAAE"), PROGRAM_FEEDBACK("012500000009G9VAAU"),
    NEW_PROGRAM_REQUEST("012500000009G9LAAU"), NEW_PRODUCT_REQUEST("012500000009G9GAAU"),
    PRODUCT_ENHANCEMENT_REQUEST("012500000009G9QAAU");

    final String salesForceId;

    EmmiCaseType(String salesForceId) {
        this.salesForceId = salesForceId;
    }

    /**
     * Fetch an EmmiCaseType from a SalesForce id
     *
     * @param id the salesforce id
     * @return EmmiCaseType or null
     */
    public static EmmiCaseType fromSalesForceId(String id) {
        for (EmmiCaseType emmiCaseType : values()) {
            if (StringUtils.equalsIgnoreCase(emmiCaseType.salesForceId, id)) {
                return emmiCaseType;
            }
        }
        return null;
    }
}
