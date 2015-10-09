package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.Client;
import com.emmisolutions.emmimanager.model.configuration.ImpersonationHolder;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.UserClientPermission;
import com.emmisolutions.emmimanager.model.user.client.UserClientRole;
import com.emmisolutions.emmimanager.model.user.client.UserClientUserClientRole;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.service.security.SuperUserTeamRoleGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;

import static com.emmisolutions.emmimanager.model.user.client.UserClientPermissionName.PERM_CLIENT_SUPER_USER;

/**
 * UserDetails service that loads UserAdmin objects then converts them into
 * UserClient objects that have the PERM_CLIENT_SUPER_USER permission.
 */
@Service("impersonationUserDetailsService")
public class AdminImpersonateClientUserDetailsServiceImpl extends UserDetailsServiceImpl {

    private static final String CLIENT_ID_SEPARATOR = "~~";
    @Resource
    private transient UserAdminPersistence userPersistence;
    @Resource
    private transient ClientPersistence clientPersistence;
    @Resource
    private SuperUserTeamRoleGenerator superUserTeamRoleGenerator;


    @Override
    @Transactional(readOnly = true)
    public UserClient loadUserByUsername(final String login) {

        String[] loginWithClientId = StringUtils.splitByWholeSeparator(login, CLIENT_ID_SEPARATOR);
        String loginToLoad;

        if (loginWithClientId != null && loginWithClientId.length == 2) {
            loginToLoad = loginWithClientId[0];
            if (StringUtils.isNumeric(loginWithClientId[1])) {
                ImpersonationHolder.setClientId(Long.parseLong(loginWithClientId[1]));
            }
        } else {
            loginToLoad = login;
        }

        UserAdmin trueLogin = userPersistence.fetchUserWillFullPermissions(loginToLoad);
        if (trueLogin == null || CollectionUtils.isEmpty(trueLogin.getAuthorities())) {
            throw new UsernameNotFoundException("User " + login + " was not found.");
        }

        // load the client from thread local storage
        Client client = clientPersistence.reload(new Client(ImpersonationHolder.getClientId()));
        if (client == null) {
            throw new UsernameNotFoundException("Client was not found.");
        }

        // create an impersonated UserClient from the admin
        UserClient impersonated = new UserClient();

        // set the attributes from the logged in user
        impersonated.setImpersonated(true);
        final UserClientUserClientRole role = new UserClientUserClientRole();
        role.setUserClientRole(new UserClientRole("impersonated", client, new HashSet<UserClientPermission>() {{
            add(new UserClientPermission(PERM_CLIENT_SUPER_USER));
        }}));
        impersonated.setClientRoles(new HashSet<UserClientUserClientRole>() {{
            add(role);
        }});

        impersonated.setFirstName(trueLogin.getFirstName());
        impersonated.setLastName(trueLogin.getLastName());
        impersonated.setEmail(trueLogin.getEmail());
        impersonated.setId(trueLogin.getId());
        impersonated.setVersion(trueLogin.getVersion());
        impersonated.setType("A");
        impersonated.setPassword("***********impersonated_user***********");
        impersonated.setActivated(true);
        impersonated.setCredentialsNonExpired(true);
        impersonated.setEmailValidated(true);
        impersonated.setAccountNonExpired(true);
        impersonated.setAccountNonLocked(true);
        impersonated.setActive(true);
        impersonated.setClient(client);
        impersonated.setLogin(trueLogin.getLogin() + CLIENT_ID_SEPARATOR + client.getId());
        impersonated.setTeamRoles(superUserTeamRoleGenerator.allPermissionsOnAllTeams(client));
        return impersonated;
    }

}
