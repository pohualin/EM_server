package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPasswordHistory;
import com.emmisolutions.emmimanager.model.user.client.activation.ActivationRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ChangePasswordRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordHistoryService;
import com.emmisolutions.emmimanager.service.UserClientPasswordValidationService;

/**
 * Implementation of UserClientValidationService
 */
@Service
public class UserClientPasswordValidationServiceImpl implements
        UserClientPasswordValidationService {

    @Resource
    private UserClientPersistence userClientPersistence;

    @Resource
    UserClientPasswordHistoryService userClientPasswordHistoryService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;

    @Override
    @Transactional(readOnly = true)
    public List<UserClientPasswordValidationError> validateRequest(
            ChangePasswordRequest changePasswordRequest) {

        if (StringUtils.isBlank(changePasswordRequest.getLogin())
                || StringUtils.isBlank(changePasswordRequest
                        .getExistingPassword())
                || StringUtils.isBlank(changePasswordRequest.getNewPassword())) {
            throw new InvalidDataAccessApiUsageException(
                    "Required fields can not be null.");
        }

        UserClient existing = userClientPersistence
                .fetchUserWillFullPermissions(changePasswordRequest.getLogin());
        if (existing == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }

        ClientPasswordConfiguration configuration = clientPasswordConfigurationService
                .get(existing.getClient());

        List<UserClientPasswordValidationError> errors = new ArrayList<UserClientPasswordValidationError>();

        // Check if user is eligible to change password
        if (!isEligibleForPasswordChange(configuration, existing)) {
            errors.add(new UserClientPasswordValidationError(
                    UserClientPasswordValidationService.Reason.DAYS_BETWEEN));
        }

        // Check if password matches pattern
        if (!validatePasswordPattern(configuration,
                changePasswordRequest.getNewPassword())) {
            errors.add(new UserClientPasswordValidationError(
                    UserClientPasswordValidationService.Reason.POLICY));
        }

        // Check if password repeats
        if (!isPasswordNotRepeatsHistory(configuration, existing,
                changePasswordRequest.getNewPassword())) {
            errors.add(new UserClientPasswordValidationError(
                    UserClientPasswordValidationService.Reason.HISTORY));
        }

        return errors;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserClientPasswordValidationError> validateRequest(
            ExpiredPasswordChangeRequest expiredPasswordChangeRequest) {

        if (StringUtils.isBlank(expiredPasswordChangeRequest.getLogin())
                || StringUtils.isBlank(expiredPasswordChangeRequest
                        .getExistingPassword())
                || StringUtils.isBlank(expiredPasswordChangeRequest
                        .getNewPassword())) {
            throw new InvalidDataAccessApiUsageException(
                    "Required fields can not be null.");
        }

        UserClient existing = userClientPersistence
                .fetchUserWillFullPermissions(expiredPasswordChangeRequest
                        .getLogin());
        if (existing == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }

        ClientPasswordConfiguration configuration = clientPasswordConfigurationService
                .get(existing.getClient());

        List<UserClientPasswordValidationError> errors = new ArrayList<UserClientPasswordValidationError>();

        // Check if password matches pattern
        if (!validatePasswordPattern(configuration,
                expiredPasswordChangeRequest.getNewPassword())) {
            errors.add(new UserClientPasswordValidationError(
                    UserClientPasswordValidationService.Reason.POLICY));
        }

        // Check if password repeats
        if (!isPasswordNotRepeatsHistory(configuration, existing,
                expiredPasswordChangeRequest.getNewPassword())) {
            errors.add(new UserClientPasswordValidationError(
                    UserClientPasswordValidationService.Reason.HISTORY));
        }

        return errors;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserClientPasswordValidationError> validateRequest(
            ResetPasswordRequest resetPasswordRequest) {

        if (StringUtils.isBlank(resetPasswordRequest.getResetToken())
                || StringUtils.isBlank(resetPasswordRequest.getNewPassword())) {
            throw new InvalidDataAccessApiUsageException(
                    "Required fields can not be null.");
        }

        UserClient existing = userClientPersistence
                .findByResetToken(resetPasswordRequest.getResetToken());
        if (existing == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing reset token");
        }

        ClientPasswordConfiguration configuration = clientPasswordConfigurationService
                .get(existing.getClient());

        List<UserClientPasswordValidationError> errors = new ArrayList<UserClientPasswordValidationError>();

        // Check if password matches pattern
        if (!validatePasswordPattern(configuration,
                resetPasswordRequest.getNewPassword())) {
            errors.add(new UserClientPasswordValidationError(
                    UserClientPasswordValidationService.Reason.POLICY));
        }

        // Check if password repeats
        if (!isPasswordNotRepeatsHistory(configuration, existing,
                resetPasswordRequest.getNewPassword())) {
            errors.add(new UserClientPasswordValidationError(
                    UserClientPasswordValidationService.Reason.HISTORY));
        }

        return errors;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserClientPasswordValidationError> validateRequest(
            ActivationRequest activationRequest) {

        if (StringUtils.isBlank(activationRequest.getActivationToken())
                || StringUtils.isBlank(activationRequest.getNewPassword())) {
            throw new InvalidDataAccessApiUsageException(
                    "Required fields can not be null.");
        }

        UserClient existing = userClientPersistence
                .findByActivationKey(activationRequest.getActivationToken());
        if (existing == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing activation token");
        }

        ClientPasswordConfiguration configuration = clientPasswordConfigurationService
                .get(existing.getClient());

        List<UserClientPasswordValidationError> errors = new ArrayList<UserClientPasswordValidationError>();

        // Check if password matches pattern
        if (!validatePasswordPattern(configuration,
                activationRequest.getNewPassword())) {
            errors.add(new UserClientPasswordValidationError(
                    UserClientPasswordValidationService.Reason.POLICY));
        }

        // Check if password repeats
        if (!isPasswordNotRepeatsHistory(configuration, existing,
                activationRequest.getNewPassword())) {
            errors.add(new UserClientPasswordValidationError(
                    UserClientPasswordValidationService.Reason.HISTORY));
        }

        return errors;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPasswordNotRepeatsHistory(
            ClientPasswordConfiguration configuration, UserClient existing,
            String password) {
        if (configuration == null || existing == null
                || StringUtils.isBlank(password)) {
            return false;
        }

        List<UserClientPasswordHistory> historiesToCheck = new ArrayList<UserClientPasswordHistory>();
        Page<UserClientPasswordHistory> histories = userClientPasswordHistoryService
                .get(new PageRequest(0, configuration.getPasswordRepetitions()),
                        existing);
        if (!histories.hasContent()) {
            UserClientPasswordHistory current = new UserClientPasswordHistory();
            current.setPassword(existing.getPassword());
            current.setSalt(existing.getSalt());
            historiesToCheck.add(current);
        } else {
            historiesToCheck.addAll(histories.getContent());
        }

        // Return UserClientPasswordValidationError if new password repeats
        for (UserClientPasswordHistory history : historiesToCheck) {
            if (passwordEncoder.matches(password, history.getPassword()
                    + history.getSalt())) {
                return false;
            }
        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEligibleForPasswordChange(
            ClientPasswordConfiguration configuration, UserClient existing) {
        if (configuration == null || existing == null) {
            return false;
        }

        if (existing.getPasswordSavedDateTime() == null) {
            return true;
        }

        if (LocalDateTime.now(DateTimeZone.UTC)
                .minusDays(configuration.getDaysBetweenPasswordChange())
                .isAfter(existing.getPasswordSavedDateTime())) {
            return true;
        }

        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validatePasswordPattern(
            ClientPasswordConfiguration configuration, String password) {
        if (configuration == null || StringUtils.isBlank(password)) {
            return false;
        }

        if (configuration.getPasswordLength() > password.length()) {
            return false;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("^");
            if (configuration.hasLowercaseLetters()) {
                sb.append("(?=.*[a-z])");
            }
            if (configuration.hasUppercaseLetters()) {
                sb.append("(?=.*[A-Z])");
            }
            if (configuration.hasNumbers()) {
                sb.append("(?=.*[0-9])");
            }
            if (configuration.hasSpecialChars()) {
                sb.append("(?=.*(_|[^\\w]))");
            }
            sb.append(".+$");
            Pattern p = Pattern.compile(sb.toString());
            Matcher m = p.matcher(password);
            return m.matches();
        }
    }

}
