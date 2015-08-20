package com.emmisolutions.emmimanager.web.rest.admin.security.cas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * CAS Variables as a dependency
 */
@Component
public class CasVariables {

    @Value("${cas.service.validation.client.uri:/webapi-client/j_spring_cas_security_check}")
    private String casClientValidationUri;
    @Value("${cas.service.validation.uri:/webapi/j_spring_cas_security_check}")
    private String casAdminValidationUri;
    @Value("${cas.username.suffix:@emmisolutions.com}")
    private String userNameSuffix;
    @Value("${cas.provider.key:12234245632699}")
    private String casProviderKey;
    @Value("${cas.server.url:https://devcas1.emmisolutions.com/cas}")
    private String casServerUrl;
    @Value("${cas.server.login.url:https://devcas1.emmisolutions.com/cas/login}")
    private String casServerLoginUrl;

    public String getUserNameSuffix() {
        return userNameSuffix;
    }

    public String getCasProviderKey() {
        return casProviderKey;
    }

    public String getCasClientValidationUri() {
        return casClientValidationUri;
    }

    public String getCasAdminValidationUri() {
        return casAdminValidationUri;
    }

    public String getCasServerUrl() {
        return casServerUrl;
    }

    public String getCasServerLoginUrl() {
        return casServerLoginUrl;
    }
}
