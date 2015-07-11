package com.emmisolutions.emmimanager.model.salesforce;

import static com.emmisolutions.emmimanager.model.salesforce.FieldType.TEXTAREA;

/**
 * Specific textarea string case field
 */
public class TextAreaCaseField extends StringCaseField {

    public TextAreaCaseField() {
        setType(TEXTAREA);
    }
}
