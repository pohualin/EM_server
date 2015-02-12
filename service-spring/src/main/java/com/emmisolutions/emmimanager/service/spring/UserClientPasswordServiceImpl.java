package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.spring.security.LegacyPasswordEncoder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Implementation of UserClientPasswordService
 */
@Service
public class UserClientPasswordServiceImpl implements UserClientPasswordService {

    @Resource
    private UserClientPersistence userClientPersistence;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void updatePassword(UserClient user) {
        UserClient userClient = userClientPersistence.reload(user);
        if (userClient == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }
        userClient.setCredentialsNonExpired(false);
        userClient.setPassword(user.getPassword());
        // set the expiration length based upon the user never logging in
        userClient.setPasswordResetExpirationDateTime(LocalDateTime.now(DateTimeZone.UTC)
                .plusHours(userClient.isNeverLoggedIn() ? UserClientService.ACTIVATION_TOKEN_HOURS_VALID :
                        RESET_TOKEN_HOURS_VALID));
        userClientPersistence.saveOrUpdate(encodePassword(userClient));
    }

    @Override
    @Transactional
    public UserClient changeExpiredPassword(ExpiredPasswordChangeRequest expiredPasswordChangeRequest) {
        if (expiredPasswordChangeRequest != null &&
                StringUtils.isNotBlank(expiredPasswordChangeRequest.getNewPassword()) &&
                StringUtils.isNotBlank(expiredPasswordChangeRequest.getLogin()) &&
                StringUtils.isNotBlank(expiredPasswordChangeRequest.getExistingPassword())) {
            UserClient userClient = userClientPersistence.fetchUserWillFullPermissions(expiredPasswordChangeRequest.getLogin());
            if (userClient != null &&
                    isValid(userClient.getPasswordResetExpirationDateTime()) &&
                    !userClient.isCredentialsNonExpired() &&
                    passwordEncoder.matches(expiredPasswordChangeRequest.getExistingPassword(),
                            userClient.getPassword() + userClient.getSalt())) {
                // user exists, credentials are expired, before reset expiration date and existing password is correct
                userClient.setPassword(expiredPasswordChangeRequest.getNewPassword());
                userClient.setCredentialsNonExpired(true);
                userClient.setPasswordResetExpirationDateTime(null);
                userClient.setPasswordResetToken(null);
                return userClientPersistence.saveOrUpdate(encodePassword(userClient));
            }
        }
        return null;
    }

    @Override
    public UserClient encodePassword(UserClient userClient) {
        String encodedPasswordPlusSalt = passwordEncoder.encode(userClient.getPassword());
        userClient.setPassword(encodedPasswordPlusSalt.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        userClient.setSalt(encodedPasswordPlusSalt.substring(LegacyPasswordEncoder.PASSWORD_SIZE));
        return userClient;
    }

    @Override
    @Transactional
    public UserClient resetPassword(ResetPasswordRequest resetPasswordRequest) {
        UserClient ret = null;
        if (resetPasswordRequest != null) {
            UserClient userClient =
                    userClientPersistence.findByResetToken(resetPasswordRequest.getResetToken());
            if (userClient != null) {
                LocalDateTime expiration = userClient.getPasswordResetExpirationDateTime();
                userClient.setPasswordResetToken(null);
                userClient.setPasswordResetExpirationDateTime(null);
                if (isValid(expiration)) {
                    // the token on the user is valid, set the password and validate the email
                    userClient.setPassword(resetPasswordRequest.getNewPassword());
                    userClient.setCredentialsNonExpired(true);
                    userClient.setEmailValidated(true);
                    ret = userClientPersistence.saveOrUpdate(encodePassword(userClient));
                } else {
                    userClientPersistence.saveOrUpdate(userClient);
                }
            }
        }
        return ret;
    }

    private boolean isValid(LocalDateTime expiration) {
        return expiration == null || LocalDateTime.now(DateTimeZone.UTC).isBefore(expiration);
    }

    @Override
    @Transactional
    public UserClient addResetTokenTo(UserClient userClient) {
        UserClient fromDb = userClientPersistence.reload(userClient);
        if (fromDb == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }
        // create the reset token and timestamp
        fromDb.setPasswordResetExpirationDateTime(LocalDateTime.now(DateTimeZone.UTC)
                .plusHours(RESET_TOKEN_HOURS_VALID));
        fromDb.setPasswordResetToken(passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(40))
                .substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        return userClientPersistence.saveOrUpdate(fromDb);
    }

    @Override
    @Transactional
    public UserClient forgotPassword(String email) {
        if (email != null) {
            UserClient userClient = userClientPersistence.findByEmail(email);
            if (userClient != null) {
                return addResetTokenTo(userClient);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public UserClient expireResetToken(UserClient userClient) {
        UserClient fromDb = userClientPersistence.reload(userClient);
        if (fromDb == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }
        // set the timestamp back a year or so
        fromDb.setPasswordResetExpirationDateTime(LocalDateTime.now(DateTimeZone.UTC)
                .minusHours(RESET_TOKEN_HOURS_VALID).minusYears(1));
        return userClientPersistence.saveOrUpdate(fromDb);
    }

}
