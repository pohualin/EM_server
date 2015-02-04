package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.mail.EmailTemplate;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring data repo for EmailTemplate objects
 */
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long>, JpaSpecificationExecutor<EmailTemplate> {

    /**
     * Find an EmailTemplate by its type
     *
     * @param type to find
     * @return an EmailTemplate object or null
     */
    EmailTemplate findByType(EmailTemplateType type);
}
