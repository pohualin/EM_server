package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.EmailRestrictConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.activation.ActivationRequest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.ClientRestrictConfigurationService;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.EmailRestrictConfigurationService;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.spring.security.LegacyPasswordEncoder;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * It can only contact the persistence layer and is responsible for Transaction
 * demarcation. This layer will also have security annotations at the method
 * level as well.
 */
@Service
public class UserClientServiceImpl implements UserClientService {

    @Resource
    ClientService clientService;

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientPasswordService userClientPasswordService;
    
    @Resource
    ClientRestrictConfigurationService clientRestrictConfigurationService;
    
    @Resource
    EmailRestrictConfigurationService emailRestrictConfigurationService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserClient create(UserClient userClient) {
        // ensure new creation
        userClient.setId(null);
        userClient.setVersion(null);

        // no password
        userClient.setPassword(null);
        userClient.setSalt(null);

        // user is not activated
        userClient.setActivated(false);
        userClient.setEmailValidated(false);

        return userClientPersistence.saveOrUpdate(userClient);
    }

    @Override
    public UserClient reload(UserClient userClient) {
        return userClientPersistence.reload(userClient);
    }

    @Override
    @Transactional
    public UserClient update(UserClient userClient) {
        UserClient inDb = reload(userClient);
        if (inDb == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }

        // do not allow for security related fields to be changed on update
        userClient.setClient(inDb.getClient());
        userClient.setPassword(inDb.getPassword());
        userClient.setSalt(inDb.getSalt());
        userClient.setActivationKey(inDb.getActivationKey());
        userClient.setActivated(inDb.isActivated());
        userClient.setCredentialsNonExpired(inDb.isCredentialsNonExpired());
        userClient.setAccountNonExpired(inDb.isAccountNonExpired());
        userClient.setAccountNonLocked(inDb.isAccountNonLocked());
        userClient.setPasswordResetToken(inDb.getPasswordResetToken());
        userClient.setPasswordResetExpirationDateTime(inDb.getPasswordResetExpirationDateTime());
        // validation should be false if the email address has changed, otherwise set it to whatever it was previously
        userClient.setEmailValidated(
                StringUtils.equalsIgnoreCase(userClient.getEmail(), inDb.getEmail()) && inDb.isEmailValidated());
        return userClientPersistence.saveOrUpdate(userClient);
    }

    @Override
    @Transactional
    public Page<UserClient> list(Pageable pageable,
                                 UserClientSearchFilter filter) {
        return userClientPersistence.list(pageable, filter);
    }

    @Override
    public List<UserClientConflict> findConflictingUsers(UserClient userClient) {
        List<UserClientConflict> ret = new ArrayList<>();
        for (UserClient conflict : userClientPersistence
                .findConflictingUsers(userClient)) {
            if (StringUtils.equalsIgnoreCase(userClient.getLogin(),
                    conflict.getLogin())) {
                ret.add(new UserClientConflict(Reason.LOGIN, conflict));
            } else if (StringUtils.equalsIgnoreCase(userClient.getEmail(),
                    conflict.getEmail())) {
                ret.add(new UserClientConflict(Reason.EMAIL, conflict));
            }
        }
        return ret;
    }

    @Override
    @Transactional
    public UserClient activate(ActivationRequest activationRequest) {
        UserClient ret = null;
        if (activationRequest != null) {
            UserClient userClient =
                    userClientPersistence.findByActivationKey(activationRequest.getActivationToken());
            if (userClient != null) {
                LocalDateTime expiration = userClient.getActivationExpirationDateTime();
                userClient.setActivationKey(null);
                userClient.setActivationExpirationDateTime(null);
                if (isValid(expiration)) {
                    // activation key (and timestamp) is valid
                    userClient.setActivated(true);
                    userClient.setEmailValidated(true);
                    userClient.setPassword(activationRequest.getNewPassword());
                    userClient.setCredentialsNonExpired(true);
                    ret = userClientPersistence.saveOrUpdate(userClientPasswordService.encodePassword(userClient));
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
    public UserClient addActivationKey(UserClient userClient) {
        UserClient fromDb = reload(userClient);
        if (fromDb == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }
        // update the activation key, only for not yet activated users
        UserClient ret = null;
        if (!fromDb.isActivated()) {
            fromDb.setActivationExpirationDateTime(LocalDateTime.now(DateTimeZone.UTC)
                    .plusHours(ACTIVATION_TOKEN_HOURS_VALID));
            fromDb.setActivationKey(
                    passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(40))
                            .substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
            ret = userClientPersistence.saveOrUpdate(fromDb);
        }
        return ret;
    }

    @Override
    public UserClient expireActivationToken(UserClient userClient) {
        UserClient fromDb = reload(userClient);
        if (fromDb == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }
        // set the timestamp back a year or so
        fromDb.setActivationExpirationDateTime(LocalDateTime.now(DateTimeZone.UTC)
                .minusHours(ACTIVATION_TOKEN_HOURS_VALID).minusYears(1));
        return userClientPersistence.saveOrUpdate(fromDb);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateEmailAddress(UserClient userClient) {
        ClientRestrictConfiguration restrictConfig = clientRestrictConfigurationService
                .getByClient(userClient.getClient());

        if (restrictConfig.isEmailConfigRestrict() == false) {
            // return true if isEmailConfigRestrict returns false
            return true;
        } else {
            Page<EmailRestrictConfiguration> validEmailEndingPage = emailRestrictConfigurationService
                    .getByClient(null, userClient.getClient());
            List<String> validEmailEndings = collectAllValidEmailEndings(null,
                    validEmailEndingPage, userClient);

            if (validEmailEndings.size() == 0) {
                // return null if no valid email ending is set
                return true;
            } else {
                // parse email to get domain
                String domain = StringUtils.substringAfter(
                        userClient.getEmail(), "@");

                // Get the last two parts of domain separated by "."
                int count = StringUtils.countMatches(domain, ".");
                String trimmedDomain = "";
                if (count == 1) {
                    trimmedDomain = domain;
                } else if (count > 1) {
                    trimmedDomain = domain.substring(StringUtils
                            .lastOrdinalIndexOf(domain, ".", 2) + 1);
                }

                if (validEmailEndings.contains(trimmedDomain)) {
                    // return true if validEmailEndings contain the trimmed
                    // domain
                    return true;
                } else {
                    // return false if email does not match valid endings
                    return false;
                }
            }
        }
    }
    
    private List<String> collectAllValidEmailEndings(
            List<String> listOfValidEmailEndings,
            Page<EmailRestrictConfiguration> validEmailEndingPage,
            UserClient userClient) {
        if (listOfValidEmailEndings == null) {
            listOfValidEmailEndings = new ArrayList<String>();
        }
        for (EmailRestrictConfiguration validEmailEnding : validEmailEndingPage
                .getContent()) {
            listOfValidEmailEndings.add(validEmailEnding.getEmailEnding());
        }
        if (validEmailEndingPage.hasNext()) {
            Page<EmailRestrictConfiguration> nextPage = emailRestrictConfigurationService
                    .getByClient(validEmailEndingPage.nextPageable(),
                            userClient.getClient());
            collectAllValidEmailEndings(listOfValidEmailEndings, nextPage,
                    userClient);
        }
        return listOfValidEmailEndings;
    }

}
