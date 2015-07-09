package com.emmisolutions.emmimanager.model.salesforce;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.EMAIL;

/**
 *
 */
public class EmailCaseField extends StringCaseField {

    public EmailCaseField() {
        setType(EMAIL);
    }

}
