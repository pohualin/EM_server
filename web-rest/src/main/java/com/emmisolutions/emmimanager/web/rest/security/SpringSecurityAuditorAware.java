package com.emmisolutions.emmimanager.web.rest.security;

import com.emmisolutions.emmimanager.config.Constants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    public String getCurrentAuditor() {
        String userName = getCurrentLogin();
        return (userName != null ? userName : Constants.SYSTEM_ACCOUNT);
    }

    private String getCurrentLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails springSecurityUser;
        String userName = null;

        if (authentication.getPrincipal() instanceof UserDetails) {
            springSecurityUser = (UserDetails) authentication.getPrincipal();
            userName = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            userName = (String) authentication.getPrincipal();
        }

        return userName;
    }
}
