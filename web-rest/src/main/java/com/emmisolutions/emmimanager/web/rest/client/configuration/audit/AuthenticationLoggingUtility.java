package com.emmisolutions.emmimanager.web.rest.client.configuration.audit;

import com.emmisolutions.emmimanager.model.audit.login.LoginStatusName;
import com.emmisolutions.emmimanager.model.audit.logout.LogoutSourceName;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService.APPLICATION;

/**
 * Go-between spring security Authentication and service layer logging
 */
@Component
public class AuthenticationLoggingUtility {

    @Resource
    AuthenticationAuditService authenticationAuditService;

    public void login(Authentication authentication, LoginStatusName status, APPLICATION application) {
        User user = null;
        String ipAddress = null;
        String loginName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof User) {
                user = (User) authentication.getPrincipal();
            }
            if (authentication.getDetails() instanceof HttpProxyAwareAuthenticationDetails) {
                ipAddress = ((HttpProxyAwareAuthenticationDetails) authentication.getDetails()).getIp();
            }
            loginName = authentication.getName();
        }
        authenticationAuditService.login(loginName, user, ipAddress, status, application);
    }

    public void logout(Authentication authentication, LogoutSourceName source, APPLICATION application) {
        User user = null;
        String ipAddress = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof User) {
                user = (User) authentication.getPrincipal();
            }
            if (authentication.getDetails() instanceof HttpProxyAwareAuthenticationDetails) {
                ipAddress = ((HttpProxyAwareAuthenticationDetails) authentication.getDetails()).getIp();
            }
        }
        authenticationAuditService.logout(user, ipAddress, source, application);
    }
}
