package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.UserClientValidationEmailService;
import com.emmisolutions.emmimanager.service.spring.security.LegacyPasswordEncoder;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Implementation of UserClientValidationEmailService
 */
@Service
public class UserClientValidateEmailServiceImpl implements UserClientValidationEmailService {
    @Resource
    private UserClientPersistence userClientPersistence;

    @Resource
    PasswordEncoder passwordEncoder;

    /**
     * Add validation token to userclient for use in the validation email
     * @param userClient on which to add the validation token
     * @return Userclient with validation token
     */
    @Override
    @Transactional
    public UserClient addValidationTokenTo(UserClient userClient) {
        UserClient fromDb = userClientPersistence.reload(userClient);
        if (fromDb == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }
        // create the validation token and timestamp
        fromDb.setValidationExpirationDateTime(LocalDateTime.now(DateTimeZone.UTC)
                .plusHours(VALIDATION_TOKEN_HOURS_VALID));
        fromDb.setValidationToken(passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(40))
                .substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        return userClientPersistence.saveOrUpdate(fromDb);
    }

    /**
     * Validate token from email
     * @param validationEmailToken token to validate
     * @return Userclient loaded from validation token
     */
    @Override
    @Transactional
    public UserClient validate(String validationEmailToken) {
        UserClient ret = null;
        if (validationEmailToken != null) {
            UserClient userClient =userClientPersistence.findByValidationToken(validationEmailToken);
            if (userClient != null) {
                LocalDateTime expiration = userClient.getValidationExpirationDateTime();
                userClient.setValidationToken(null);
                if (isValid(expiration)) {
                    userClient.setEmailValidated(true);
                    ret = userClientPersistence.saveOrUpdate(userClient);
                }
            }
        }
        return ret;
    }

    private boolean isValid(LocalDateTime expiration) {
        return expiration == null || LocalDateTime.now(DateTimeZone.UTC).isBefore(expiration);
    }
}
