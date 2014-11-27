package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRole;
import com.emmisolutions.emmimanager.persistence.UserClientReferenceTeamRolePersistence;
import com.emmisolutions.emmimanager.persistence.UserClientTeamRolePersistence;
import com.emmisolutions.emmimanager.service.ClientService;
import com.emmisolutions.emmimanager.service.UserClientTeamRoleService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Spring wired implementation for UserClientTeamRoleService
 */
@Service
public class UserClientTeamRoleServiceImpl implements UserClientTeamRoleService {

    @Resource
    UserClientTeamRolePersistence userClientTeamRolePersistence;

    @Resource
    ClientService clientService;

    @Resource
    UserClientReferenceTeamRolePersistence referenceGroupPersistence;

    @Override
    public Page<UserClientTeamRole> find(Client client, Pageable page) {
        if (client == null || client.getId() == null) {
            throw new InvalidDataAccessApiUsageException("Client cannot be null");
        }
        return userClientTeamRolePersistence.find(client.getId(), page);
    }

    @Override
    @Transactional
    public UserClientTeamRole save(UserClientTeamRole userClientTeamRole) {
        return userClientTeamRolePersistence.save(userClientTeamRole);
    }

    @Override
    public UserClientTeamRole reload(UserClientTeamRole userClientTeamRole) {
        return userClientTeamRolePersistence.reload(userClientTeamRole);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<UserClientTeamPermission> loadAll(UserClientTeamRole userClientTeamRole) {
        UserClientTeamRole loaded = reload(userClientTeamRole);
        return loaded.getUserClientTeamPermissions();
    }

    @Override
    @Transactional
    public UserClientTeamRole create(UserClientTeamRole userClientTeamRole) {
        if (userClientTeamRole == null || userClientTeamRole.getId() != null ||
            userClientTeamRole.getClient() == null) {
            throw new InvalidDataAccessApiUsageException("UserClientTeamRole can neither be null nor have an id and must have a client attached");
        }
        userClientTeamRole.setId(null);
        userClientTeamRole.setClient(clientService.reload(userClientTeamRole.getClient()));
        return userClientTeamRolePersistence.save(userClientTeamRole);
    }

    @Override
    public void remove(UserClientTeamRole userClientTeamRole) {
        UserClientTeamRole toRemove = reload(userClientTeamRole);
        if (toRemove == null) {
            throw new InvalidDataAccessApiUsageException("UserClientTeamRole cannot be null");
        }
        userClientTeamRolePersistence.remove(toRemove.getId());
    }

    @Override
    public Set<UserClientTeamPermission> loadPossiblePermissions() {
        return userClientTeamRolePersistence.loadPossiblePermissions();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserClientReferenceTeamRole> loadReferenceRoles(Pageable page) {
        return referenceGroupPersistence.loadReferenceTeamRoles(page);
    }

}
