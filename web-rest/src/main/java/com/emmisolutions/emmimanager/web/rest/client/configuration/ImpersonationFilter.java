package com.emmisolutions.emmimanager.web.rest.client.configuration;

import com.emmisolutions.emmimanager.model.configuration.ImpersonationHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.emmisolutions.emmimanager.web.rest.client.configuration.ImpersonationConfiguration.TRIGGER_VALUE;

/**
 * Clears the security context as a filter when the TRIGGER_VALUE parameter or header is present. It also puts
 * the client id (the value of the TRIGGER_VALUE) into thread local.
 */
public class ImpersonationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String clientIdString =
                StringUtils.isNotBlank(request.getParameter(TRIGGER_VALUE)) ?
                        request.getParameter(TRIGGER_VALUE) : request.getHeader(TRIGGER_VALUE);

        if (StringUtils.isNumeric(clientIdString)) {
            Long clientId = Long.parseLong(clientIdString);
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(null);
            SecurityContextHolder.clearContext();
            ImpersonationHolder.setClientId(clientId);
        }
        filterChain.doFilter(request, response);
        ImpersonationHolder.clear();
    }



}
