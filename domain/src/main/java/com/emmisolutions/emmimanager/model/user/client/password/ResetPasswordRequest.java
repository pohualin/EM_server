package com.emmisolutions.emmimanager.model.user.client.password;

/**
 * Object used for UserClient Password resetting
 */
public class ResetPasswordRequest {

    private String resetToken;
    private String newPassword;

    public ResetPasswordRequest() {
    }

    /**
     * Construct with an reset token and password
     *
     * @param resetToken key
     * @param password   password
     */
    public ResetPasswordRequest(String resetToken, String password) {
        this.resetToken = resetToken;
        this.newPassword = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
}
