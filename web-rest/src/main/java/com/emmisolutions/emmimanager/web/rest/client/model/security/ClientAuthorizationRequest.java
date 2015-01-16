package com.emmisolutions.emmimanager.web.rest.client.model.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Check to see if a client level permission is present in the authorities.
 */
@Component("client")
public class ClientAuthorizationRequest extends AuthorizationRequest {

    private Long clientId;

    private ClientAuthorizationRequest() {
    }

    private ClientAuthorizationRequest(Long clientId) {
        this.clientId = clientId;
    }

    /**
     * Make a new ClientAuthorizationRequest instance.
     * This method is named this way to make SPeL code look good.
     *
     * @param clientId the client id
     * @return a new instance of this class
     */
    public ClientAuthorizationRequest _new(Long clientId) {
        return new ClientAuthorizationRequest(clientId);
    }

    @Override
    protected boolean checkPermission(String permission, Collection<? extends GrantedAuthority> authorities) {
        boolean hasPermission = false;
        if (clientId != null) {
            String permissionToCheckFor = permission + "_" + clientId;
            for (GrantedAuthority grantedAuthority : authorities) {
                if (permissionToCheckFor.equals(grantedAuthority.getAuthority())) {
                    hasPermission = true;
                    break;
                }
            }
        }
        return hasPermission;
    }
}
