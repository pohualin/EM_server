package com.emmisolutions.emmimanager.model.user.client.activation;

/**
 * Object used for UserClient Activation
 */
public class ActivationRequest {

    private String activationToken;
    private String newPassword;

    public ActivationRequest() {
    }

    /**
     * Construct with an activation key and password
     *
     * @param activationKey key
     * @param password      password
     */
    public ActivationRequest(String activationKey, String password) {
        this.activationToken = activationKey;
        this.newPassword = password;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
