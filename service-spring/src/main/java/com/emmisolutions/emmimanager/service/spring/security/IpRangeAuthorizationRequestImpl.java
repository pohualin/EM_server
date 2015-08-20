package com.emmisolutions.emmimanager.service.spring.security;

import com.emmisolutions.emmimanager.model.configuration.ClientRestrictConfiguration;
import com.emmisolutions.emmimanager.model.configuration.IpRestrictConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.service.ClientRestrictConfigurationService;
import com.emmisolutions.emmimanager.service.IpRestrictConfigurationService;
import com.emmisolutions.emmimanager.service.audit.HttpProxyAwareAuthenticationDetails;
import com.emmisolutions.emmimanager.service.security.IpRangeAuthorizationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Determines if the authenticated user is within a particular ip range configured for the client.
 */
@Component("ipRangeWithinClientConfiguration")
public class IpRangeAuthorizationRequestImpl implements IpRangeAuthorizationRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpRangeAuthorizationRequestImpl.class);

    @Resource
    IpRestrictConfigurationService ipRestrictConfigurationService;

    @Resource
    ClientRestrictConfigurationService clientRestrictConfigurationService;

    /**
     * Check if the authentication detail object's ip address is in the acceptable range for the
     * client in which the userClient is located.
     *
     * @param userClient to find the Client
     * @param details    to check the range with
     * @return true user is within the allowed range, false not allowed
     */
    public boolean withinClientAllowedRange(UserClient userClient, HttpProxyAwareAuthenticationDetails details) {
        boolean isWithinAllowedRange;

        if (userClient.isImpersonated()) {
            LOGGER.debug("{} is an impersonated user and will bypass ip address restrictions", userClient.getClient());
            isWithinAllowedRange = true;
        } else {
            ClientRestrictConfiguration clientRestrictConfiguration =
                    clientRestrictConfigurationService.getByClient(userClient.getClient());
            if (clientRestrictConfiguration != null && clientRestrictConfiguration.isIpConfigRestrict()) {
                // pull the first page of restrictions
                Page<IpRestrictConfiguration> pageOfRestrictions =
                        ipRestrictConfigurationService.getByClient(null, userClient.getClient());
                if (pageOfRestrictions.getTotalElements() > 0) {
                    Boolean userValidForPage = null;
                    while (pageOfRestrictions != null) {
                        userValidForPage = processPageOfRestrictions(userClient, details, pageOfRestrictions);
                        if (!Boolean.TRUE.equals(userValidForPage)) {
                            // ip address was not valid for page of rules, get next page if we can
                            pageOfRestrictions = pageOfRestrictions.hasNext() ?
                                    ipRestrictConfigurationService.getByClient(
                                            pageOfRestrictions.nextPageable(), userClient.getClient()) : null;
                        } else {
                            // ip address was valid within the page, no need to get next page
                            pageOfRestrictions = null;
                        }
                    }
                    if (userValidForPage == null) {
                        LOGGER.info("{} has ip range restriction enabled but has no valid ranges configured", userClient.getClient());
                        isWithinAllowedRange = true;
                    } else {
                        // all pages have been processed, set the return value
                        isWithinAllowedRange = Boolean.TRUE.equals(userValidForPage);
                    }
                } else {
                    LOGGER.debug("{} has ip range restriction enabled but no ip ranges configured", userClient.getClient());
                    isWithinAllowedRange = true;
                }
            } else {
                LOGGER.debug("{} has ip range restriction disabled", userClient.getClient());
                isWithinAllowedRange = true;
            }
        }
        return isWithinAllowedRange;
    }

    /**
     * Process a page of IpRestrictConfiguration objects
     *
     * @param userClient         for logging purposes
     * @param details            to check if it falls within the ip ranges
     * @param pageOfRestrictions that hold the ip ranges
     * @return true when details match a range, false when details do not match the range, null when no ranges were valid
     * or there is no ip address for the user
     */
    private Boolean processPageOfRestrictions(UserClient userClient, HttpProxyAwareAuthenticationDetails details,
                                              Page<IpRestrictConfiguration> pageOfRestrictions) {
        Boolean ret = null;
        for (IpRestrictConfiguration ipRestrictConfiguration : pageOfRestrictions) {
            if (Boolean.TRUE.equals(ret)) {
                // user checks out, no need to continue
                break;
            }
            String start = ipRestrictConfiguration.getIpRangeStart();
            String end = ipRestrictConfiguration.getIpRangeEnd();
            switch (details.checkBoundaries(start, end)) {
                case IN_RANGE:
                    LOGGER.debug("{} ip address {} is allowed; it is between {} and {}", userClient, details.getIp(), start, end);
                    ret = true;
                    break;
                case OUT_OF_BOUNDS:
                    LOGGER.debug("{} ip address {} is not between {} and {}", userClient, details.getIp(), start, end);
                    ret = false;
                    break;
                case INVALID_RANGE:
                    LOGGER.info("Invalid configuration: {}", ipRestrictConfiguration);
                    break;
                case NO_IP:
                    LOGGER.warn("No ip address found for {}", userClient);
                    break;
            }
        }
        return ret;
    }
}
