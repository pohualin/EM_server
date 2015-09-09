package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.mail.EmailTemplate;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateTracking;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateType;
import com.emmisolutions.emmimanager.model.user.client.UserClient;

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

    /**
     * Log a sent email
     *
     * @param emailTemplate that will be sent
     * @param userClient    who it is sent to
     * @return the EmailSent object
     */
    EmailTemplateTracking log(EmailTemplate emailTemplate, UserClient userClient);
}
