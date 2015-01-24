package com.emmisolutions.emmimanager.model.user.client.password;

/**
 * Object used when an expired password needs to be changed
 */
public class ExpiredPasswordChangeRequest {

    private String login;
    private String existingPassword;
    private String newPassword;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getExistingPassword() {
        return existingPassword;
    }

    public void setExistingPassword(String existingPassword) {
        this.existingPassword = existingPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
