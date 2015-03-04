package com.emmisolutions.emmimanager.web.rest.client.configuration.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Custom permission evaluator so we can ensure the user has rights to access a particular client
 * and/or team.
 */
@Component
public class AuthorizationRequestPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (targetDomainObject instanceof UserAuthorizationRequest){
            UserAuthorizationRequest userAuthorizationRequest = (UserAuthorizationRequest) targetDomainObject;
            return userAuthorizationRequest.isLoggedIn(permission, authentication);
        }
        if (targetDomainObject instanceof UserKnowsPasswordAuthorizationRequest){
            UserKnowsPasswordAuthorizationRequest pwRequest = (UserKnowsPasswordAuthorizationRequest) targetDomainObject;
            return pwRequest.isLoggedInAndKnowsPassword(permission, authentication);
        }
        if (targetDomainObject instanceof AuthorizationRequest){
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) targetDomainObject;
            return authorizationRequest.hasPermission(permission, authentication);
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
