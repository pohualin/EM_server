package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.spring.security.LegacyPasswordEncoder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
        userClientPersistence.saveOrUpdate(encodePassword(userClient));
    }

    @Override
    @Transactional
    public void changeExpiredPassword(ExpiredPasswordChangeRequest expiredPasswordChangeRequest) {
        if (expiredPasswordChangeRequest != null &&
                StringUtils.isNotBlank(expiredPasswordChangeRequest.getNewPassword()) &&
                StringUtils.isNotBlank(expiredPasswordChangeRequest.getLogin()) &&
                StringUtils.isNotBlank(expiredPasswordChangeRequest.getExistingPassword())) {
            UserClient userClient = userClientPersistence.fetchUserWillFullPermissions(expiredPasswordChangeRequest.getLogin());
            if (userClient != null &&
                    passwordEncoder.matches(expiredPasswordChangeRequest.getExistingPassword(),
                            userClient.getPassword() + userClient.getSalt())) {
                // user exists and existing password is correct
                if (!userClient.isCredentialsNonExpired()) {
                    // credentials are expired, process request
                    userClient.setPassword(expiredPasswordChangeRequest.getNewPassword());
                    userClient.setCredentialsNonExpired(true);
                    userClientPersistence.saveOrUpdate(encodePassword(userClient));
                }
            }
        }
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
                userClient.setPasswordResetToken(null);
                userClient.setPassword(resetPasswordRequest.getNewPassword());
                userClient.setCredentialsNonExpired(true);
                userClient.setEmailValidated(true);
                return userClientPersistence.saveOrUpdate(encodePassword(userClient));
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
        // update the activation key, only for not yet activated users
        fromDb.setPasswordResetToken(
                passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(40))
                        .substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        return userClientPersistence.saveOrUpdate(fromDb);
    }

    @Override
    public UserClient forgotPassword(String email) {
        if (email != null) {
            UserClient userClient = userClientPersistence.findByEmail(email);
            if (userClient != null){
                return addResetTokenTo(userClient);
            }
        }
        return null;
    }

}
