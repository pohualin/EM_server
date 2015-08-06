package com.emmisolutions.emmimanager.web.rest.client.configuration.audit;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

/**
 * A WebAuthenticationDetails builder that understands proxied requests
 */
public class ProxyAwareWebAuthenticationDetailsSource implements
        AuthenticationDetailsSource<HttpServletRequest, HttpProxyAwareAuthenticationDetails> {

    @Override
    public HttpProxyAwareAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new HttpProxyAwareAuthenticationDetailsImpl(request);
    }

}
