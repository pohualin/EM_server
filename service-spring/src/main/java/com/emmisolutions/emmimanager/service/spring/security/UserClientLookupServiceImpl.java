package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.security.UserClientLookupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Simple wrapper around the persistence layer
 */
@Service
public class UserClientLookupServiceImpl implements UserClientLookupService {

    @Resource
    UserClientPersistence userClientPersistence;

    @Override
    public UserClient findByActivationKey(String activationKey) {
        return userClientPersistence.findByActivationKey(activationKey);
    }

    @Override
    public UserClient findByResetToken(String resetToken) {
        return userClientPersistence.findByResetToken(resetToken);
    }

    @Override
    public UserClient findByValidationToken(String validationToken) {
        return userClientPersistence.findByValidationToken(validationToken);
    }
}
