package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ImpersonationHolder;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.*;
import com.emmisolutions.emmimanager.model.user.client.team.UserClientUserClientTeamRole;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;

import static com.emmisolutions.emmimanager.model.user.client.UserClientPermissionName.PERM_CLIENT_SUPER_USER;

/**
 * UserDetails service that loads UserAdmin objects then converts them into
 * UserClient objects that have the PERM_CLIENT_SUPER_USER permission.
 */
@Service("impersonationUserDetailsService")
public class AdminImpersonateClientUserDetailsServiceImpl extends UserDetailsServiceImpl {

    @Resource
    private UserAdminPersistence userPersistence;

    @Resource
    private ClientPersistence clientPersistence;

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(final String login) {

        UserAdmin trueLogin = userPersistence.fetchUserWillFullPermissions(login);
        if (trueLogin == null || CollectionUtils.isEmpty(trueLogin.getAuthorities())){
            throw new UsernameNotFoundException("User " + login + " was not found.");
        }

        // load the client from thread local storage
        Client client = clientPersistence.reload(ImpersonationHolder.getClientId());
        if (client == null){
            throw new UsernameNotFoundException("Client was not found.");
        }

        // create an impersonated UserClient from the admin
        UserClient impersonated = new UserClient();

        // set the attributes from the logged in user
        impersonated.setImpersonated(true);
        final UserClientUserClientRole role = new UserClientUserClientRole();
        role.setUserClientRole(new UserClientRole("impersonated", client, new HashSet<UserClientPermission>(){{
            add(new UserClientPermission(PERM_CLIENT_SUPER_USER));
        }}));
        impersonated.setClientRoles(new ArrayList<UserClientUserClientRole>(){{
            add(role);
        }});
        impersonated.setFirstName(trueLogin.getFirstName());
        impersonated.setLastName(trueLogin.getLastName());
        impersonated.setEmail(trueLogin.getEmail());
        impersonated.setActivated(true);
        impersonated.setCredentialsNonExpired(true);
        impersonated.setEmailValidated(true);
        impersonated.setAccountNonExpired(true);
        impersonated.setAccountNonLocked(true);
        impersonated.setActive(true);
        impersonated.setClient(client);
        impersonated.setLogin(trueLogin.getLogin());
        impersonated.setTeamRoles(new HashSet<UserClientUserClientTeamRole>());
        return impersonated;
    }
}
