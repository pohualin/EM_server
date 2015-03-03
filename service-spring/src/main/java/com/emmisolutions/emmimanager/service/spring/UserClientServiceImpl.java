package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.activation.ActivationRequest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.ClientPasswordConfigurationService;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.spring.security.LegacyPasswordEncoder;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * It can only contact the persistence layer and is responsible for Transaction
 * demarcation. This layer will also have security annotations at the method
 * level as well.
 */
@Service
public class UserClientServiceImpl implements UserClientService {

    @Resource
    ClientPasswordConfigurationService clientPasswordConfigurationService;
    
    @Resource
    ClientService clientService;
    
    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientPasswordService userClientPasswordService;
    
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
                    
                    ClientPasswordConfiguration configuration = findClientPasswordConfiguration(userClient);
                    userClient.setPasswordExpireationDateTime(LocalDateTime.now(
                            DateTimeZone.UTC).plusDays(
                            configuration.getPasswordExpirationDays()));
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
    
    private ClientPasswordConfiguration findClientPasswordConfiguration(UserClient userClient) {
        Client client = null;
        if (userClient != null) {
            client = userClient.getClient();
        }
        return clientPasswordConfigurationService.get(client);
    }

}
