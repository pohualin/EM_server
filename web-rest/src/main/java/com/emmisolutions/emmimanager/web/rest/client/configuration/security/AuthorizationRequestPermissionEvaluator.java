package com.emmisolutions.emmimanager.web.rest.client.configuration.security;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;
import com.emmisolutions.emmimanager.service.audit.HttpProxyAwareAuthenticationDetails;
import com.emmisolutions.emmimanager.service.security.IpRangeAuthorizationRequest;
import com.emmisolutions.emmimanager.web.rest.client.configuration.security.ip.IpRangeActivationAuthorizationRequest;
import com.emmisolutions.emmimanager.web.rest.client.configuration.security.ip.IpRangeResetAuthorizationRequest;
import com.emmisolutions.emmimanager.web.rest.client.configuration.security.ip.IpRangeValidateEmailAuthorizationRequest;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Custom permission evaluator so we can ensure the user has rights to access a particular client
 * and/or team.
 */
@Component
public class AuthorizationRequestPermissionEvaluator extends DenyAllPermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject != null) {
            if (targetDomainObject instanceof UserAuthorizationRequest) {
                return ((UserAuthorizationRequest) targetDomainObject).isLoggedIn(permission, authentication);
            }
            if (targetDomainObject instanceof UserKnowsPasswordAuthorizationRequest) {
                return ((UserKnowsPasswordAuthorizationRequest) targetDomainObject)
                        .isLoggedInAndKnowsPassword(permission, authentication);
            }
            if (targetDomainObject instanceof AuthorizationRequest) {
                return ((AuthorizationRequest) targetDomainObject).hasPermission(permission, authentication);
            }
            if (targetDomainObject instanceof UserSecurityResponseForResetPasswordRequest &&
                    permission instanceof ResetPasswordRequest) {
                return ((UserSecurityResponseForResetPasswordRequest) targetDomainObject)
                        .isSecurityResponseValid((ResetPasswordRequest) permission);
            }
            if (targetDomainObject instanceof IpRangeActivationAuthorizationRequest &&
                    authentication.getDetails() instanceof HttpProxyAwareAuthenticationDetails) {
                return ((IpRangeActivationAuthorizationRequest) targetDomainObject)
                        .withinClientAllowedRange(permission.toString(),
                                (HttpProxyAwareAuthenticationDetails) authentication.getDetails());
            }
            if (targetDomainObject instanceof IpRangeResetAuthorizationRequest &&
                    authentication.getDetails() instanceof HttpProxyAwareAuthenticationDetails) {
                return ((IpRangeResetAuthorizationRequest) targetDomainObject)
                        .withinClientAllowedRange(permission.toString(),
                                (HttpProxyAwareAuthenticationDetails) authentication.getDetails());
            }
            if (targetDomainObject instanceof IpRangeValidateEmailAuthorizationRequest &&
                    authentication.getDetails() instanceof HttpProxyAwareAuthenticationDetails) {
                return ((IpRangeValidateEmailAuthorizationRequest) targetDomainObject)
                        .withinClientAllowedRange(permission.toString(),
                                (HttpProxyAwareAuthenticationDetails) authentication.getDetails());
            }
            if (targetDomainObject instanceof IpRangeAuthorizationRequest) {
                IpRangeAuthorizationRequest ipRangeAuthorizationRequest = (IpRangeAuthorizationRequest) targetDomainObject;
                if (authentication.getPrincipal() instanceof UserClient) {
                    if (authentication.getDetails() instanceof HttpProxyAwareAuthenticationDetails) {
                        return ipRangeAuthorizationRequest.withinClientAllowedRange((UserClient) authentication.getPrincipal(),
                                (HttpProxyAwareAuthenticationDetails) authentication.getDetails());
                    } else {
                        // don't check ip unless the details are specific
                        return true;
                    }
                }
            }
        }
        return super.hasPermission(authentication, targetDomainObject, permission);
    }

}
