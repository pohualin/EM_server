package com.emmisolutions.emmimanager.web.rest.admin.security.cas;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.cas.web.authentication.ServiceAuthenticationDetails;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.emmisolutions.emmimanager.web.rest.admin.configuration.CasSecurityConfiguration.SERVICE_REQUEST_URL_PARAMETER;

/**
 * Allows us to save the query string and retrieve it later.
 * The query string has the originally requested URL in it as a parameter.
 */
public class SavedUrlServiceAuthenticationDetails implements ServiceAuthenticationDetails {

    private String redirectUrl;
    private String serviceUrl;
    private String encodedRedirectUrl;

    /**
     * Create a SavedUrlServiceAuthenticationDetails.
     *
     * @param request    used to extract and decode the SERVICE_REQUEST_URL_PARAMETER
     * @param serviceUrl which gets appended the SERVICE_REQUEST_URL_PARAMETER if it is present
     */
    public SavedUrlServiceAuthenticationDetails(HttpServletRequest request, String serviceUrl) {
        Base64 urlSafeBase64 = new Base64(-1, null, true);
        this.encodedRedirectUrl = StringUtils.trimToNull(
                splitQuery(request).get(SERVICE_REQUEST_URL_PARAMETER));

        if (StringUtils.isNotBlank(encodedRedirectUrl)) {
            // decode the encodedRedirect to use later
            this.redirectUrl = new String(urlSafeBase64.decode(encodedRedirectUrl));
            UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(serviceUrl);
            uriComponentsBuilder.queryParam(SERVICE_REQUEST_URL_PARAMETER, encodedRedirectUrl);
            this.serviceUrl = uriComponentsBuilder.build(false).toString();
        } else {
            // no redirect url, use straight up service url
            this.serviceUrl = serviceUrl;
        }
    }

    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    @Override
    public String getServiceUrl() {
        return this.serviceUrl;
    }

    private Map<String, String> splitQuery(HttpServletRequest request) {
        Map<String, String> query_pairs = new LinkedHashMap<>();
        String query = request.getQueryString();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                        URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // no-op
            }
        }
        return query_pairs;
    }
}