package com.emmisolutions.emmimanager.web.rest.client.configuration.audit;

import com.emmisolutions.emmimanager.service.audit.HttpProxyAwareAuthenticationDetails;
import org.apache.commons.lang3.text.StrTokenizer;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

import static com.emmisolutions.emmimanager.service.audit.HttpProxyAwareAuthenticationDetails.RANGES.*;


/**
 * Pulls the ip address from the request
 */
public class HttpProxyAwareAuthenticationDetailsImpl implements HttpProxyAwareAuthenticationDetails {

    public static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    public static final Pattern pattern = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");

    private String ip;

    /**
     * This constructs the details based upon the servlet request. It does so by
     * examining the request headers for the remote user's ip address.
     * <p/>
     * It looks through the following headers (in this order):
     * X-Forwarded-For, Proxy-Client-IP, WL-Proxy-Client-IP, HTTP_CLIENT_IP, HTTP_X_FORWARDED_FOR
     * <p/>
     * If none of the above headers are present the request.getRemoteAddr() is used.
     * <p/>
     * Sometimes multiple IP addresses are passed via the headers in a comma delimited
     * way. The first non-private (or loopback) ip address present will be used in
     * this case. If there is no non-private address present, the private address will
     * be set as the ip.
     *
     * @param request to examine
     */
    public HttpProxyAwareAuthenticationDetailsImpl(HttpServletRequest request) {
        ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String privateIpAddress = null;
        if (ip != null) {
            // if the ip string has multiple addresses pick the first valid ip address
            StrTokenizer tokenizer = new StrTokenizer(ip, ",");
            while (tokenizer.hasNext()) {
                ip = tokenizer.nextToken().trim();
                if (isIPv4Valid(ip)) {
                    if (isIPv4Private(ip)) {
                        privateIpAddress = ip;
                        ip = null;
                    }
                    break;
                } else {
                    ip = null;
                }
            }
        }
        if (ip == null) {
            // there are no public addresses, set to private if that is only ip address present
            ip = privateIpAddress;
        }
    }

    /**
     * Convert an ip address into a long
     *
     * @param ip the ipv4 address
     * @return a long
     */
    public long ipV4ToLong(String ip) {
        String[] octets = ip.split("\\.");
        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16) +
                (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
    }

    /**
     * Determines if an ipv4 address is private or loopback
     *
     * @param ip to check
     * @return true if in a private network or loopback address
     */
    public boolean isIPv4Private(String ip) {
        long longIp = ipV4ToLong(ip);
        return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255")) || // single class A network
                (longIp >= ipV4ToLong("127.0.0.1") && longIp <= ipV4ToLong("127.255.255.254")) || // loop-back
                (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255")) || // 16 contiguous class B networks
                longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255"); // 256 contiguous class C networks
    }

    private boolean isIPv4Valid(String ip) {
        return pattern.matcher(ip).matches();
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public RANGES checkBoundaries(String lowerBoundary, String upperBoundary) {
        if (ip != null) {
            if (isIPv4Valid(lowerBoundary) && isIPv4Valid(upperBoundary)) {
                long lowerBound = ipV4ToLong(lowerBoundary);
                long upperBound = ipV4ToLong(upperBoundary);
                if (lowerBound <= upperBound) {
                    long longIp = ipV4ToLong(ip);
                    if (longIp >= lowerBound && longIp <= upperBound) {
                        return IN_RANGE;
                    } else {
                        return OUT_OF_BOUNDS;
                    }
                } else {
                    return INVALID_RANGE;
                }
            } else {
                return INVALID_RANGE;
            }
        }
        return NO_IP;
    }

//    private String longToIpV4(long longIp) {
//        int octet3 = (int) ((longIp >> 24) % 256);
//        int octet2 = (int) ((longIp >> 16) % 256);
//        int octet1 = (int) ((longIp >> 8) % 256);
//        int octet0 = (int) ((longIp) % 256);
//        return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
//    }

}
