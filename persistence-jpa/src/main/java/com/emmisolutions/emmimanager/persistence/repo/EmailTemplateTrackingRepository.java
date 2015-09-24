package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateTracking;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateType;
import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring data repo for EmailSent objects
 */
public interface EmailTemplateTrackingRepository extends JpaRepository<EmailTemplateTracking, Long>,
        JpaSpecificationExecutor<EmailTemplateTracking> {

    /**
     * Finds a tracking by signature
     *
     * @param signature to use to find
     * @return the persistent tracker
     */
    EmailTemplateTracking findBySignature(String signature);

    /**
     * Find how many emails have been sent to a patient
     *
     * @param emailTemplateType the type of email
     * @param patient           sent to
     * @param sentAfter         this date
     * @return the total
     */
    Long countByEmailTemplateTypeAndPatientAndCreatedDateAfter(EmailTemplateType emailTemplateType,
                                                               Patient patient,
                                                               DateTime sentAfter);
}
