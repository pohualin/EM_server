package com.emmisolutions.emmimanager.web.rest.admin.security.cas;

import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService;
import com.emmisolutions.emmimanager.web.rest.client.configuration.audit.AuthenticationLoggingUtility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.emmisolutions.emmimanager.model.audit.login.LoginStatusName.SUCCESS;
import static com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService.APPLICATION.ADMIN_FACING;
import static com.emmisolutions.emmimanager.web.rest.client.configuration.ImpersonationConfiguration.TRIGGER_VALUE;

/**
 * Spring Security success handler, specialized for CAS requests.
 */
@Component
public class CasAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Resource(name ="adminTokenBasedRememberMeServices")
    TokenBasedRememberMeServices adminTokenBasedRememberMeServices;

    @Resource
    private AuthenticationLoggingUtility authenticationLoggingUtility;
    /*
     * A redirect strategy that is impersonation aware
     */
    private RedirectStrategy redirectStrategy = new RedirectStrategy() {
        public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
            response.sendRedirect(UriComponentsBuilder.fromUriString(url)
                    .replaceQueryParam(TRIGGER_VALUE) // remove in case of impersonation
                    .build(true)
                    .toString());
        }
    };

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        authenticationLoggingUtility.login(authentication, SUCCESS, getApplication());

        if (authentication.getPrincipal() instanceof UserAdmin) {
            // bridge CAS success to Remember me success
            adminTokenBasedRememberMeServices.onLoginSuccess(request, response, authentication);
        }

        if (authentication.getDetails() instanceof SavedUrlServiceAuthenticationDetails) {
            // pull the place to redirect to from the saved URL which was recorded prior to service ticket creation
            SavedUrlServiceAuthenticationDetails rememberWebAuthenticationDetails = (SavedUrlServiceAuthenticationDetails) authentication.getDetails();
            if (StringUtils.isNotBlank(rememberWebAuthenticationDetails.getRedirectUrl())) {
                getRedirectStrategy().sendRedirect(request, response, rememberWebAuthenticationDetails.getRedirectUrl());
                return;
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

    protected AuthenticationAuditService.APPLICATION getApplication() {
        return ADMIN_FACING;
    }

    @Override
    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

}
