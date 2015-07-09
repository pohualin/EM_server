package com.emmisolutions.emmimanager.model.salesforce;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.PHONE;

/**
 * Phone number field
 */
public class PhoneCaseField extends StringCaseField {

    public PhoneCaseField() {
        setType(PHONE);
    }
}
