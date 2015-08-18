package com.emmisolutions.emmimanager.web.rest.client.configuration.security.ip;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.audit.HttpProxyAwareAuthenticationDetails;
import com.emmisolutions.emmimanager.service.security.IpRangeAuthorizationRequest;
import com.emmisolutions.emmimanager.service.security.UserClientLookupService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Checks that the reset request comes from a machine within the client configuration
 * for that reset request
 */
@Component("resetWithinIpRange")
public class IpRangeResetAuthorizationRequest {

    @Resource
    IpRangeAuthorizationRequest ipRangeAuthorizationRequest;

    @Resource
    UserClientLookupService userClientLookupService;

    /**
     * If the reset request resolves to a user client, test with the usual ip range
     * tester
     *
     * @param resetToken used to locate a user client
     * @param details    the authentication
     * @return true when there isn't a UserClient found, results of IpRangeAuthorizationRequest otherwise
     * @see IpRangeAuthorizationRequest#withinClientAllowedRange(UserClient, HttpProxyAwareAuthenticationDetails)
     */
    public boolean withinClientAllowedRange(String resetToken,
                                            HttpProxyAwareAuthenticationDetails details) {

        UserClient userClient = userClientLookupService.findByResetToken(resetToken);
        return userClient == null || ipRangeAuthorizationRequest.withinClientAllowedRange(userClient, details);

    }
}
