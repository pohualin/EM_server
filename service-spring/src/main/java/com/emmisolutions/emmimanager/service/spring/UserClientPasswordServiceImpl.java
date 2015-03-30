package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ChangePasswordRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordHistoryService;
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
    private UserClientPasswordHistoryService userClientPasswordHistoryService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;

    @Override
    @Transactional
    public UserClient updatePassword(UserClient user, boolean setCredentialNonExpired) {
        UserClient userClient = userClientPersistence.reload(user);
        if (userClient == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }
        // unlock a user that is locked
        UserClient unlockedUser = userClientPersistence.unlockUserClient(userClient);
        unlockedUser.setCredentialsNonExpired(setCredentialNonExpired);
        unlockedUser.setPassword(user.getPassword());
        // set the expiration length based upon the user never logging in
        unlockedUser.setPasswordResetExpirationDateTime(LocalDateTime.now(DateTimeZone.UTC)
                .plusHours(userClient.isNeverLoggedIn() ? UserClientService.ACTIVATION_TOKEN_HOURS_VALID :
                        RESET_TOKEN_HOURS_VALID));
        return userClientPersistence.saveOrUpdate(encodePassword(unlockedUser));
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
                UserClient unlockedUser = userClientPersistence.unlockUserClient(userClient);
                // user exists, credentials are expired, before reset expiration date and existing password is correct
                unlockedUser.setPassword(expiredPasswordChangeRequest.getNewPassword());
                unlockedUser.setCredentialsNonExpired(true);
                unlockedUser.setPasswordResetExpirationDateTime(null);
                unlockedUser.setPasswordResetToken(null);
                
                UserClient passwordUpdatedUser = updatePasswordExpirationTime(encodePassword(unlockedUser));
                
                userClientPasswordHistoryService
                        .handleUserClientPasswordHistory(passwordUpdatedUser);
                
                return passwordUpdatedUser;
            }
        }
        return null;
    }
    
    @Override
    public UserClient changePassword(ChangePasswordRequest changePasswordRequest) {
        UserClient toUpdate = userClientPersistence
                .fetchUserWillFullPermissions(changePasswordRequest.getLogin());
        toUpdate.setPassword(changePasswordRequest.getNewPassword());
        toUpdate = updatePassword(toUpdate, true);
        toUpdate = updatePasswordExpirationTime(toUpdate);

        userClientPasswordHistoryService
                .handleUserClientPasswordHistory(toUpdate);
        
        return toUpdate;
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
        if (resetPasswordRequest != null) {
            UserClient userClient =
                    userClientPersistence.findByResetToken(resetPasswordRequest.getResetToken());
            if (userClient != null) {
                LocalDateTime expiration = userClient.getPasswordResetExpirationDateTime();
                userClient.setPasswordResetToken(null);
                userClient.setPasswordResetExpirationDateTime(null);
                if (isValid(expiration)) {
                    UserClient unlockedUser = userClientPersistence.unlockUserClient(userClient);
                    // the token on the user is valid, set the password and validate the email
                    unlockedUser.setPassword(resetPasswordRequest.getNewPassword());
                    unlockedUser.setCredentialsNonExpired(true);
                    unlockedUser.setEmailValidated(true);
                    
                    UserClient passwordUpdatedUser = updatePasswordExpirationTime(encodePassword(unlockedUser));
                    
                    userClientPasswordHistoryService
                            .handleUserClientPasswordHistory(passwordUpdatedUser);
                    
                    return passwordUpdatedUser;
                } else {
                    // don't return this just update to remove the reset token
                    userClientPersistence.saveOrUpdate(userClient);
                }
            }
        }
        return null;
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
                ClientPasswordConfiguration configuration = findClientPasswordConfiguration(userClient);
                if (configuration.isPasswordReset()) {
                    return addResetTokenTo(userClient);
                } else {
                    userClient.setPasswordResetToken(null);
                    return userClient;
                }
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
    
    @Override
    @Transactional(readOnly = true)
    public ClientPasswordConfiguration findPasswordPolicyUsingResetToken(String resetToken) {
        return findClientPasswordConfiguration(
                userClientPersistence.findByResetToken(resetToken));
    }

    @Override
    @Transactional(readOnly = true)
    public ClientPasswordConfiguration findPasswordPolicyUsingActivationToken(String activationToken) {
        return findClientPasswordConfiguration(
                userClientPersistence.findByActivationKey(activationToken));
    }

    private ClientPasswordConfiguration findClientPasswordConfiguration(UserClient userClient) {
        Client client = null;
        if (userClient != null) {
            client = userClient.getClient();
        }
        return clientPasswordConfigurationService.get(client);
    }

    private boolean isValid(LocalDateTime expiration) {
        return expiration == null || LocalDateTime.now(DateTimeZone.UTC).isBefore(expiration);
    }

    @Override
    @Transactional
    public UserClient updatePasswordExpirationTime(UserClient userClient) {
        ClientPasswordConfiguration configuration = findClientPasswordConfiguration(userClient);
        userClient.setPasswordExpireationDateTime(LocalDateTime.now(
                DateTimeZone.UTC).plusDays(
                configuration.getPasswordExpirationDays()));
        userClient
                .setPasswordSavedDateTime(LocalDateTime.now(DateTimeZone.UTC));
        return userClientPersistence.saveOrUpdate(userClient);
    }

}
