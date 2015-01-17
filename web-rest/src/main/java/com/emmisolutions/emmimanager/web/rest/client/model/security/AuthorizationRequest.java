package com.emmisolutions.emmimanager.web.rest.client.model.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * This class knows how to check if an authorized user has the required permission
 */
public abstract class AuthorizationRequest {

    /**
     * Method that is called by the AuthorizationRequestPermissionEvaluator to
     * validate if an authenticated user has the declared permission
     *
     * @param permission     to ensure exists within the authentication
     * @param authentication the logged in authenticated user
     * @return boolean if they have permission or not
     */
    public boolean hasPermission(Object permission, Authentication authentication) {
        if (authentication == null) {
            // must be authenticated
            return false;
        }
        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        if (CollectionUtils.isEmpty(grantedAuthorities)) {
            // must have some granted authorities, without them not authorized
            return false;
        }
        if (permission == null) {
            // if there is no permission to check against, user is authorized
            return true;
        }
        String permissionString = permission.toString();
        // if there is no permission to check against, user is authorized;
        // when there is a permission delegate to subclass
        return StringUtils.isBlank(permissionString) || checkPermission(permissionString, grantedAuthorities);
    }

    /**
     * Ensure that the authorities contain the permission
     *
     * @param permission  to be looked for
     * @param authorities to look in
     * @return true if the permission exists, false if it doesn't
     */
    protected abstract boolean checkPermission(String permission, Collection<? extends GrantedAuthority> authorities);
}
