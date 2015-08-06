package com.emmisolutions.emmimanager.web.rest.client.configuration.audit;

/**
 * Authentication Details that is http proxy aware.
 */
public interface HttpProxyAwareAuthenticationDetails {

    /**
     * Returns the IP address of the authenticated user
     *
     * @return the ipv4 address string
     */
    String getIp();

    /**
     * Determine if the authenticated user is within the ip range
     *
     * @param lowerBoundary lower bounds
     * @param upperBoundary upper bounds
     * @return RANGES result
     */
    RANGES checkBoundaries(String lowerBoundary, String upperBoundary);

    /**
     * Possible boundary check responses
     */
    enum RANGES {
        OUT_OF_BOUNDS, INVALID_RANGE, IN_RANGE, NO_IP
    }
}

