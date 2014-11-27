package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.model.user.client.reference.UserClientReferenceRole;
import com.emmisolutions.emmimanager.persistence.UserClientReferenceRolePersistence;
import com.emmisolutions.emmimanager.persistence.UserClientRolePersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.UserClientRoleService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Spring wired implementation for UserClientRoleService
 */
@Service
public class UserClientRoleServiceImpl implements UserClientRoleService {

    @Resource
    UserClientRolePersistence userClientRolePersistence;

    @Resource
    UserClientReferenceRolePersistence userClientReferenceRolePersistence;

    @Resource
    ClientService clientService;

    @Override
    public Page<UserClientRole> find(Client client, Pageable page) {
        if (client == null || client.getId() == null) {
            throw new InvalidDataAccessApiUsageException("Client cannot be null");
        }
        return userClientRolePersistence.find(client.getId(), page);
    }

    @Override
    @Transactional
    public UserClientRole update(UserClientRole userClientRole) {
        UserClientRole inDb = userClientRolePersistence.reload(userClientRole);
        if (inDb == null){
            throw new InvalidDataAccessApiUsageException("This method is only to be used with existing UserClientRole objects");
        }
        userClientRole.setType(inDb.getType());
        return userClientRolePersistence.save(userClientRole);
    }

    @Override
    public UserClientRole reload(UserClientRole userClientRole) {
        return userClientRolePersistence.reload(userClientRole);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<UserClientPermission> loadAll(UserClientRole userClientRole) {
        UserClientRole loaded = reload(userClientRole);
        return loaded.getUserClientPermissions();
    }

    @Override
    @Transactional
    public UserClientRole create(UserClientRole userClientRole) {
        if (userClientRole == null || userClientRole.getId() != null ||
            userClientRole.getClient() == null) {
            throw new InvalidDataAccessApiUsageException("UserClientRole can neither be null nor have an id and must have a client attached");
        }
        userClientRole.setId(null);
        userClientRole.setClient(clientService.reload(userClientRole.getClient()));
        return userClientRolePersistence.save(userClientRole);
    }

    @Override
    public void remove(UserClientRole userClientRole) {
        UserClientRole toRemove = reload(userClientRole);
        if (toRemove == null) {
            throw new InvalidDataAccessApiUsageException("UserClientRole cannot be null");
        }
        userClientRolePersistence.remove(toRemove.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserClientReferenceRole> loadReferenceRoles(Pageable page) {
        return userClientReferenceRolePersistence.loadReferenceRoles(page);
    }

    @Override
    public Set<UserClientPermission> loadPossiblePermissions() {
        return userClientRolePersistence.loadPossiblePermissions();
    }

}
