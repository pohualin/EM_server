package com.emmisolutions.emmimanager.model.configuration;

/**
 * Thread local holder for client id
 */
public class ImpersonationHolder {

    private static final ThreadLocal<Long> clientIdHolder = new ThreadLocal<>();

    /**
     * Gets the thread local client id or null if there isn't one
     *
     * @return the value in thread local
     */
    public static Long getClientId() {
        return clientIdHolder.get();
    }

    /**
     * Sets the client id to the local thread
     *
     * @param clientId to set
     */
    public static void setClientId(Long clientId) {
        clientIdHolder.set(clientId);
    }

    /**
     * Clear the thread local
     */
    public static void clear() {
        clientIdHolder.remove();
    }
}
