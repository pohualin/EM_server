package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.mail.EmailTemplate;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateType;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import com.emmisolutions.emmimanager.persistence.repo.EmailTemplateRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Spring data implementation of the EmailTemplatePersistence api
 */
@Repository
public class EmailTemplatePersistenceImpl implements EmailTemplatePersistence {

    @Resource
    EmailTemplateRepository emailTemplateRepository;

    @Override
    public EmailTemplate find(EmailTemplateType type) {
        return emailTemplateRepository.findByType(type);
    }

}
