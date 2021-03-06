package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.persistence.UserClientUserClientRolePersistence;
import com.emmisolutions.emmimanager.service.UserClientRoleService;
import com.emmisolutions.emmimanager.service.UserClientService;
import com.emmisolutions.emmimanager.service.UserClientUserClientRoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * 
 * 
 */
@Service
public class UserClientUserClientRoleServiceImpl implements
        UserClientUserClientRoleService {

    @Resource
    UserClientService userClientService;

    @Resource
    UserClientRoleService userClientRoleService;

    @Resource
    UserClientUserClientRolePersistence userClientUserClientRolePersistence;

    @Override
    @Transactional
    public UserClientUserClientRole create(
            UserClientUserClientRole userClientUserClientRole) {
        userClientUserClientRole.setUserClient(userClientService
                .reload(userClientUserClientRole.getUserClient()));
        userClientUserClientRole.setUserClientRole(userClientRoleService
                .reload(userClientUserClientRole.getUserClientRole()));
        return userClientUserClientRolePersistence
                .saveOrUpdate(userClientUserClientRole);
    }

    @Override
    @Transactional
    public Page<UserClientUserClientRole> findByUserClient(
            UserClient userClient, Pageable pageable) {
        userClient = userClientService.reload(userClient);
        return userClientUserClientRolePersistence.findByUserClient(userClient,
                pageable);
    }

    @Override
    @Transactional
    public UserClientUserClientRole reload(
            UserClientUserClientRole userClientUserClientRole) {
        if (userClientUserClientRole == null
                || userClientUserClientRole.getId() == null) {
            return null;
        }
        return userClientUserClientRolePersistence
                .reload(userClientUserClientRole.getId());
    }

    @Override
    @Transactional
    public void delete(UserClientUserClientRole userClientUserClientRole) {
        if (userClientUserClientRole != null
                && userClientUserClientRole.getId() != null) {
            userClientUserClientRolePersistence.delete(userClientUserClientRole
                    .getId());
        }
    }

}
