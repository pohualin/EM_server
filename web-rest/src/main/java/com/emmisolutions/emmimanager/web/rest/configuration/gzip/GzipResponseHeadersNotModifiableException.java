package com.emmisolutions.emmimanager.web.rest.configuration.gzip;

import javax.servlet.ServletException;

/**
 * Exception for gzip response
 */
public class GzipResponseHeadersNotModifiableException extends ServletException {

    /**
     * Constructor
     *
     * @param message for the exception
     */
    public GzipResponseHeadersNotModifiableException(String message) {
        super(message);
    }
}
