package com.emmisolutions.emmimanager.web.rest.admin.security.cas;

/**
 * Thrown when XHR requests need to redirect. We can't use a
 * normal redirect in a lot of cases where the domain is different.
 */
public class XhrNeedsRedirectException extends RuntimeException {

    private String url;

    /**
     * Constructor that takes a URL
     *
     * @param url to push into the exception
     */
    public XhrNeedsRedirectException(String url) {
        super(url);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
