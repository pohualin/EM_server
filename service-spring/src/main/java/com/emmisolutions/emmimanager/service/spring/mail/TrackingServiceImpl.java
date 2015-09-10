package com.emmisolutions.emmimanager.service.spring.mail;

import com.emmisolutions.emmimanager.model.mail.EmailTemplateTracking;
import com.emmisolutions.emmimanager.persistence.EmailTemplatePersistence;
import com.emmisolutions.emmimanager.service.mail.TrackingService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Spring service implementation of the email tracking service
 */
@Service
public class TrackingServiceImpl implements TrackingService {

    @Resource
    EmailTemplatePersistence emailTemplatePersistence;

    @Override
    @Transactional
    public void viewed(String signature) {
        EmailTemplateTracking tracker = emailTemplatePersistence.load(signature);
        if (tracker != null && !tracker.isViewed()) {
            // this is the first time the email has been viewed
            tracker.setViewed(true);
            tracker.setViewedDate(DateTime.now(DateTimeZone.UTC));
        }
    }
}
