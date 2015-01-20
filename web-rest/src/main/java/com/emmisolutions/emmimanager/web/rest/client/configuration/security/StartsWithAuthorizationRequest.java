package com.emmisolutions.emmimanager.web.rest.client.configuration.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Allows us to see if the authenticated user has a permission that startsWith
 * the permission.
 */
@Component("startsWith")
public class StartsWithAuthorizationRequest extends AuthorizationRequest {


    private StartsWithAuthorizationRequest() {
    }

    @Override
    protected boolean checkPermission(String permission, Collection<? extends GrantedAuthority> authorities) {
        boolean hasPermission = false;
        if (StringUtils.isNotBlank(permission)) {
            for (GrantedAuthority grantedAuthority : authorities) {
                if (grantedAuthority.getAuthority().startsWith(permission)) {
                    hasPermission = true;
                    break;
                }
            }
        }
        return hasPermission;
    }
}
