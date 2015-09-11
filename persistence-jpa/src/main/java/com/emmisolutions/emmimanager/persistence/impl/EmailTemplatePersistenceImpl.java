package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.mail.EmailTemplate;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateTracking;
import com.emmisolutions.emmimanager.model.mail.EmailTemplateType;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.repo.EmailTemplateRepository;
import com.emmisolutions.emmimanager.persistence.repo.EmailTemplateTrackingRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Spring data implementation of the EmailTemplatePersistence api
 */
@Repository
public class EmailTemplatePersistenceImpl implements EmailTemplatePersistence {

    @Resource
    EmailTemplateRepository emailTemplateRepository;

    @Resource
    EmailTemplateTrackingRepository emailTemplateTrackingRepository;

    @Resource
    UserClientPersistence userClientPersistence;

    @Override
    public EmailTemplate find(EmailTemplateType type) {
        return emailTemplateRepository.findByType(type);
    }

    @Override
    public EmailTemplateTracking log(EmailTemplate emailTemplate, UserClient userClient) {
        EmailTemplateTracking toSave = new EmailTemplateTracking();
        toSave.setEmailTemplate(emailTemplate);
        toSave.setUserClient(userClientPersistence.reload(userClient));
        if (userClient != null) {
            toSave.setEmail(userClient.getEmail());
        }
        EmailTemplateTracking saved = emailTemplateTrackingRepository.save(toSave);
        saved.setSignature(encoder.encodePassword(saved.getId() + SERVER_SIDE_SECRET,
                saved.getCreatedDate().getMillis()));
        return emailTemplateTrackingRepository.save(saved);
    }

    @Override
    public EmailTemplateTracking load(String signature) {
        if (StringUtils.length(signature) == STORED_LENGTH) {
            return emailTemplateTrackingRepository.findBySignature(signature);
        }
        return null;
    }


}
