package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.mail.EmailTemplate;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateType;

/**
 * EmailTemplate persistence layer.
 */
public interface EmailTemplatePersistence {

    /**
     * Finds an EmailTemplate by type
     *
     * @param type of template to find
     * @return the email template or null
     */
    EmailTemplate find(EmailTemplateType type);
}
