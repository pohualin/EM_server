package com.emmisolutions.emmimanager.web.rest.admin.security.cas;

import com.emmisolutions.emmimanager.web.rest.client.resource.UserClientsPasswordResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.web.authentication.ServiceAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.emmisolutions.emmimanager.web.rest.client.configuration.ImpersonationConfiguration.TRIGGER_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Dynamically creates an authentication details that allows us retain the original url requested across
 * CAS redirets.
 */
@Component
@Scope(value = "prototype")
public class DynamicAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, ServiceAuthenticationDetails> {

    private List<String> validCasServerHostEndings;

    @Value("${cas.valid.server.suffixes:emmisolutions.com, localhost}")
    private void setValidCasServerHostEndings(String endings) {
        validCasServerHostEndings = new ArrayList<>();
        for (String ending : StringUtils.split(endings, ",")) {
            if (StringUtils.isNotBlank(ending)) {
                validCasServerHostEndings.add(StringUtils.trim(ending));
            }
        }
    }

    private ServiceProperties serviceProperties;

    public void setServiceProperties(ServiceProperties serviceProperties){
        this.serviceProperties = serviceProperties;
    }

    @Override
    public ServiceAuthenticationDetails buildDetails(HttpServletRequest context) {
        final String url = makeDynamicUrlFromRequest(context, serviceProperties);
        boolean valid = false;

        // ensure URL is from a known service
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .build(false);
        for (String validCasServerHostEnding : validCasServerHostEndings) {
            if (uriComponents.getHost()
                    .endsWith(validCasServerHostEnding)) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            throw new AccessDeniedException("The server is unable to authenticate the requested url.");
        }
        return new SavedUrlServiceAuthenticationDetails(context, uriComponents.toString());
    }

    /**
     * This mehod
     * @param request
     * @param serviceProperties
     * @return
     */
    public static String makeDynamicUrlFromRequest(HttpServletRequest request, ServiceProperties serviceProperties) {
        // ensure URL is from a known service
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(
                linkTo(methodOn(UserClientsPasswordResource.class)
                        .forgotPassword(null)).withSelfRel().getHref())
                .replacePath(serviceProperties.getService());

        // add impersonation query params, if necessary
        if (StringUtils.isNotBlank(request.getParameter(TRIGGER_VALUE))){
            builder
                    .queryParam(TRIGGER_VALUE,
                            request.getParameter(TRIGGER_VALUE));
        }
        return  builder
                .build(false)
                .toString();
    }
}
