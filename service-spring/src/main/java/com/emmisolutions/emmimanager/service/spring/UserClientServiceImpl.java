package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.activation.ActivationRequest;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.UserClientPasswordService;
import com.emmisolutions.emmimanager.service.UserClientService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    ClientService clientService;

    @Resource
    UserClientPersistence userClientPersistence;

    @Resource
    UserClientPasswordService userClientPasswordService;

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
    public UserClient activate(ActivationRequest activationRequest) {
        UserClient userClient = null;
        if (activationRequest != null) {
            userClient = userClientPersistence.findByActivationKey(activationRequest.getActivationToken());
            if (userClient != null) {
                userClient.setActivated(true);
                userClient.setActivationKey(null);
                userClient.setPassword(activationRequest.getNewPassword());
                userClient.setCredentialsNonExpired(true);
                userClientPersistence.saveOrUpdate(
                        userClientPasswordService.encodePassword(userClient));
            }
        }
        return userClient;
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
        if (!fromDb.isActivated()) {
            fromDb.setActivationKey(RandomStringUtils.randomAlphanumeric(40));
        }
        return userClientPersistence.saveOrUpdate(fromDb);
    }

}
