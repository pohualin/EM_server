package com.emmisolutions.emmimanager.service.spring;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ChangePasswordRequest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
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
    PasswordEncoder passwordEncoder;

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;

    @Override
    @Transactional(readOnly = true)
    public List<UserClientPasswordValidationError> validateRequest(
            ChangePasswordRequest changePasswordRequest) {

        if (changePasswordRequest == null
                || StringUtils.isBlank(changePasswordRequest.getLogin())
                || StringUtils.isBlank(changePasswordRequest
                        .getExistingPassword())
                || StringUtils.isBlank(changePasswordRequest.getNewPassword())) {
            throw new InvalidDataAccessApiUsageException(
                    "Required fields can not be null.");
        }

        List<UserClientPasswordValidationError> errors = new ArrayList<UserClientPasswordValidationError>();

        UserClient toVerify = new UserClient();
        toVerify.setLogin(changePasswordRequest.getLogin());
        toVerify.setPassword(changePasswordRequest.getExistingPassword());

        // Check if password matches pattern
        toVerify.setPassword(changePasswordRequest.getNewPassword());
        UserClientPasswordValidationError validatePasswordPatternError = validatePasswordPattern(toVerify);
        if (validatePasswordPatternError != null) {
            errors.add(validatePasswordPatternError);
        }

        return errors;
    }

    @Override
    @Transactional(readOnly = true)
    public UserClientPasswordValidationError validatePasswordPattern(
            UserClient userClient) {
        if (userClient == null || StringUtils.isBlank(userClient.getPassword())) {
            throw new InvalidDataAccessApiUsageException(
                    "UserClient can not be null. Password to be verified is required.");
        }

        UserClient existing = userClientPersistence
                .fetchUserWillFullPermissions(userClient.getLogin());
        if (existing == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }

        ClientPasswordConfiguration configuration = clientPasswordConfigurationService
                .get(existing.getClient());
        // Return UserClientPasswordValidationError if password pattern does not
        // match
        if (!validatePasswordPattern(configuration, userClient.getPassword())) {
            return new UserClientPasswordValidationError(
                    UserClientPasswordValidationService.Reason.POLICY);
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validatePasswordPattern(
            ClientPasswordConfiguration configuration, String password) {

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
