package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.mail.EmailTemplateTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring data repo for EmailSent objects
 */
public interface EmailTemplateTrackingRepository extends JpaRepository<EmailTemplateTracking, Long>, JpaSpecificationExecutor<EmailTemplateTracking> {

}
