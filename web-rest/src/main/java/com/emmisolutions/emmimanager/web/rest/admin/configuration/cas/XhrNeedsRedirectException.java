package com.emmisolutions.emmimanager.web.rest.admin.configuration.cas;

/**
 * Thrown when XHR requests need to redirect. We can't use a
 * normal redirect in a lot of cases where the domain is different.
 */
public class XhrNeedsRedirectException extends RuntimeException {

    private String url;

    public XhrNeedsRedirectException(String url) {
        super(url);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
