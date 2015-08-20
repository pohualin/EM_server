package com.emmisolutions.emmimanager.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * When the user logs in from an invalid ip address
 */
public class IpAddressAuthenticationException extends AuthenticationException {

    /**
     * Construct the exception with a message
     *
     * @param msg the message
     */
    public IpAddressAuthenticationException(String msg) {
        super(msg);
    }
}
