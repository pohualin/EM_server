package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamPermission;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientTeamRole;
import com.emmisolutions.emmimanager.model.user.client.team.reference.UserClientReferenceTeamRole;
import com.emmisolutions.emmimanager.persistence.UserClientReferenceTeamRolePersistence;
import com.emmisolutions.emmimanager.persistence.UserClientRolePersistence;
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
    UserClientRolePersistence userClientRolePersistence;

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
    public UserClientTeamRole update(UserClientTeamRole userClientRole) {
        UserClientTeamRole inDb = userClientTeamRolePersistence.reload(userClientRole);
        if (inDb == null){
            throw new InvalidDataAccessApiUsageException("This method is only to be used with existing UserClientTeamRole objects");
        }
        userClientRole.setType(inDb.getType());
        return userClientTeamRolePersistence.save(userClientRole);
    }

    @Override
    public UserClientTeamRole reload(UserClientTeamRole userClientTeamRole) {
        return userClientTeamRolePersistence.reload(userClientTeamRole);
    }

    @Override
    public Set<UserClientTeamPermission> loadAll(UserClientTeamRole userClientTeamRole) {
        return userClientTeamRolePersistence.permissionsFor(userClientTeamRole);
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

    @Override
    @Transactional(readOnly = true)
    public boolean hasDuplicateName(UserClientTeamRole userClientTeamRole) {
        boolean hasDuplicate = false;
        if (userClientTeamRole == null || userClientTeamRole.getClient() == null) {
            throw new InvalidDataAccessApiUsageException(
                    "UserClientTeamRole can neither be null nor have an id and must have a client attached");
        }
        userClientTeamRole.setClient(clientService.reload(userClientTeamRole
                .getClient()));
        if(userClientTeamRolePersistence.findDuplicateByName(userClientTeamRole) != null){
            hasDuplicate = true;
        }
        
        // Also check the same name is not used in client role
        UserClientRole userClientRole = new UserClientRole();
        userClientRole.setName(userClientTeamRole.getName());
        userClientRole.setClient(userClientTeamRole.getClient());
        if(userClientRolePersistence.findDuplicateByName(userClientRole) != null){
            hasDuplicate = true;
        }
        return hasDuplicate;
    }    
}
