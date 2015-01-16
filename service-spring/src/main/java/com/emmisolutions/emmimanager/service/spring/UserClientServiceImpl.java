package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.spring.security.SecurityUtils;
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
    SecurityUtils securityUtils;

    @Override
    @Transactional
    public UserClient create(UserClient userClient) {
        return userClientPersistence.saveOrUpdate(userClient);
    }

    @Override
    @Transactional
    public UserClient reload(UserClient userClient) {
        if (userClient == null || userClient.getId() == null) {
            return null;
        }
        return userClientPersistence.reload(userClient.getId());
    }

    @Override
    @Transactional
    public UserClient update(UserClient userClient) {
        UserClient inDb = reload(userClient);
        if (inDb == null) {
            throw new InvalidDataAccessApiUsageException(
                    "This method is only to be used with existing UserClient objects");
        }
        // do not allow for client or password changes
        userClient.setClient(inDb.getClient());
        userClient.setPassword(inDb.getPassword());
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

}
