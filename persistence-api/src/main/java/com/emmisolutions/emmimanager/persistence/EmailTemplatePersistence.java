package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.mail.EmailTemplate;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateTracking;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateType;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * EmailTemplate persistence layer.
 */
public interface EmailTemplatePersistence {

    /**
     * Server side secret for creating the signature hash
     */
    String SERVER_SIDE_SECRET = "__server-side-secret-bits__";

    /**
     * Strength of the signature hash
     */
    int BITS = 256;

    /**
     * Length of the signature string
     */
    int STORED_LENGTH = BITS / 4;

    /**
     * The signature encoder
     */
    ShaPasswordEncoder encoder = new ShaPasswordEncoder(BITS);

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

    /**
     * Finds a persistent EmailTemplateTracking by it's signature
     *
     * @param signature to load
     * @return the persistent EmailTemplateTracking
     */
    EmailTemplateTracking load(String signature);

}
