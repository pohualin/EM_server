package com.emmisolutions.emmimanager.web.rest.admin.security.csrf;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Automatically saves generated tokens on requests that do not need to be CSRF protected.
 * The reason for this is so that we can get an initial csrf token back to the client so that
 * the first PUT/POST will have a token.
 * <p/>
 * Make sure to add this after the usual spring security CsrfTokenFilter.
 * <p/>
 * E.g.
 * <p/>
 * protected void configure(HttpSecurity http) throws Exception {
 *      http.addFilterAfter(new CsrfTokenGeneratorFilter(yourCsrfTokenRepository()), CsrfFilter.class)
 * }
 */
public final class CsrfTokenGeneratorFilter extends OncePerRequestFilter {

    final CsrfTokenRepository repository;

    private RequestMatcher requireCsrfProtectionMatcher = new DefaultRequiresCsrfMatcher();

    public CsrfTokenGeneratorFilter(CsrfTokenRepository repository) {
        this.repository = repository;
    }

    /**
     * Call save on the CsrfTokenRepository for all requests that do NOT require
     * Csrf protection
     *
     * @param request     the request
     * @param response    the response
     * @param filterChain the filter chain
     * @throws ServletException if there's a problem
     * @throws IOException      if there's a problem
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (!requireCsrfProtectionMatcher.matches(request)) {
            CsrfToken representativeToken = repository.generateToken(request);
            if (representativeToken != null) {
                CsrfToken token = (CsrfToken) request.getAttribute(representativeToken.getParameterName());
                repository.saveToken(token, request, response);
            }
        }

        filterChain.doFilter(request, response);
    }

    private static final class DefaultRequiresCsrfMatcher implements RequestMatcher {
        private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");

        public boolean matches(HttpServletRequest request) {
            return !allowedMethods.matcher(request.getMethod()).matches();
        }
    }
}
