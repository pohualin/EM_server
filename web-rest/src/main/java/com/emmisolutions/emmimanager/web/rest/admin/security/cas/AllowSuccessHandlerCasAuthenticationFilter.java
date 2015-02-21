package com.emmisolutions.emmimanager.web.rest.admin.security.cas;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This is a CasAuthenticationFilter that allows for 'success' to be fired even
 * when authenticating proxy tickets. This is really a monkey patch kind of class that
 * is necessary so that I can override the successfulAuthentication method that is for
 * some reason marked 'final' (jerks).
 */
public class AllowSuccessHandlerCasAuthenticationFilter extends CasAuthenticationFilter {

    private SessionAuthenticationStrategy sessionStrategy = new NullAuthenticatedSessionStrategy();

    private boolean continueChainBeforeSuccessfulAuthentication = false;

    /**
     * Only overriding this method so that I can call the normal successful
     * authentication method instead of the 'final' method in the
     * CasAuthenticationFilter for successfulAuthentication().
     *
     * @param req the servlet request
     * @param res the servlet response
     * @param chain the filter chain
     * @throws java.io.IOException if there's a problem
     * @throws javax.servlet.ServletException if there's a problem
     * @see org.springframework.security.cas.web.CasAuthenticationFilter#successfulAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain, org.springframework.security.core.Authentication)
     */
    @SuppressWarnings("deprecation")
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!requiresAuthentication(request, response)) {
            chain.doFilter(request, response);

            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Request is to process authentication");
        }

        Authentication authResult;

        try {
            authResult = attemptAuthentication(request, response);
            if (authResult == null) {
                // return immediately as subclass has indicated that it hasn't completed authentication
                return;
            }
            sessionStrategy.onAuthentication(authResult, request, response);
        } catch (InternalAuthenticationServiceException failed) {
            logger.error("An internal error occurred while trying to authenticate the user.", failed);
            unsuccessfulAuthentication(request, response, failed);

            return;
        } catch (AuthenticationException failed) {
            // Authentication failed
            unsuccessfulAuthentication(request, response, failed);

            return;
        }

        // Authentication success
        if (continueChainBeforeSuccessfulAuthentication) {
            chain.doFilter(request, response);
        }
        // **** This is the only override ****
        successfulAuthentication(request, response, authResult);
    }

    public void setSessionAuthenticationStrategy(SessionAuthenticationStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }

    public void setContinueChainBeforeSuccessfulAuthentication(boolean continueChainBeforeSuccessfulAuthentication) {
        this.continueChainBeforeSuccessfulAuthentication = continueChainBeforeSuccessfulAuthentication;
    }
}
