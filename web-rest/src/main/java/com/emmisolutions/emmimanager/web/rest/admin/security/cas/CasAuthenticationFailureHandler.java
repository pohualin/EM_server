package com.emmisolutions.emmimanager.web.rest.admin.security.cas;

import com.emmisolutions.emmimanager.model.audit.login.LoginStatusName;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.web.rest.client.configuration.audit.AuthenticationLoggingUtility;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.emmisolutions.emmimanager.model.audit.login.LoginStatusName.INACTIVE;
import static com.emmisolutions.emmimanager.model.audit.login.LoginStatusName.INVALID_CREDENTIALS;
import static com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService.APPLICATION.ADMIN_FACING;

/**
 * Returns a 403 error code (Forbidden) to the client, specialized for CAS requests.
 */
@Component
public class CasAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Resource
    AuthenticationLoggingUtility authenticationLoggingUtility;

    Pattern p = Pattern.compile("User (.*) was not found.");

    @Override
    @SuppressWarnings("deprecation")
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        Authentication auth = exception.getAuthentication();
        LoginStatusName status = INVALID_CREDENTIALS;

        // user logged in successfully in CAS but isn't in this system
        if (exception instanceof UsernameNotFoundException) {
            // extract the attempted user login from the error message
            Matcher m = p.matcher(exception.getMessage());
            if (m.matches()) {
                final String login = m.group(1);
                auth = update(auth, login);
            }
        }
        // when user logged in successfully to CAS but is disabled in this system
        if (exception instanceof DisabledException &&
                exception.getExtraInformation() instanceof UserAdmin) {
            // extract  attempted user login from the 'extra information'
            auth = update(auth, ((UserAdmin) exception.getExtraInformation()).getLogin());
            status = INACTIVE;
        }

        // user has tampered with the CAS service ticket, CAS doesn't return a user to us
        if (exception instanceof BadCredentialsException) {
            auth = update(auth, "unknown_cas_user");
        }

        authenticationLoggingUtility.login(auth, status, ADMIN_FACING);

        response.sendError(HttpServletResponse.SC_FORBIDDEN, exception.getClass().getSimpleName());
    }

    /**
     * Create a new authentication object using the login plus some of the original parts
     * of the passed authentication
     *
     * @param authentication two wrap
     * @param login          to use as the new id
     * @return a wrapper auth
     */
    private Authentication update(Authentication authentication, final String login) {
        final Object details = authentication.getDetails();
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return details;
            }

            @Override
            public Object getPrincipal() {
                return login;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return login;
            }
        };
    }
}
