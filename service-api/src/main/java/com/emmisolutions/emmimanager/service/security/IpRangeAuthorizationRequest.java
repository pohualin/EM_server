package com.emmisolutions.emmimanager.service.security;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.audit.HttpProxyAwareAuthenticationDetails;

/**
 * Authorization interfaces based upon ip address
 */
public interface IpRangeAuthorizationRequest {

    /**
     * Make sure the details are within the allowed ip address ranges for the client
     *
     * @param userClient to get the client
     * @param details    for the ip address
     * @return true if ok, false if out of range
     */
    boolean withinClientAllowedRange(UserClient userClient, HttpProxyAwareAuthenticationDetails details);
}
