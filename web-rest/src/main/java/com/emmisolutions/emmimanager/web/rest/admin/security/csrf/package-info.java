/**
 * This is where the CSRF/XSRF specific classes live.
 * <p/>
 * http://docs.spring.io/spring-security/site/docs/current/reference/html/csrf.html
 * <p/>
 * The main extension points deal with:
 * <p/>
 * Generation of CSRF tokens on GET requests via -
 * com.emmisolutions.emmimanager.web.rest.admin.security.csrf.CsrfTokenGeneratorFilter
 * <p/>
 * Stateless backend via -
 * com.emmisolutions.emmimanager.web.rest.admin.security.csrf.DoubleSubmitSignedCsrfTokenRepository
 * <p/>
 * Generation of json encoded 401 responses to requests with missing or invalid xsrf tokens via -
 * com.emmisolutions.emmimanager.web.rest.admin.security.csrf.CsrfAccessDeniedHandler
 * <p/>
 * XSS protection for CSRF tokens via:
 * com.emmisolutions.emmimanager.web.rest.admin.security.csrf.CsrfTokenValidationFilter
 */
package com.emmisolutions.emmimanager.web.rest.admin.security.csrf;