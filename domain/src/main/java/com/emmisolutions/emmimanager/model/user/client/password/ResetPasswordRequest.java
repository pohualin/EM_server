package com.emmisolutions.emmimanager.model.user.client.password;

import java.util.List;

import com.emmisolutions.emmimanager.model.user.client.secret.question.response.UserClientSecretQuestionResponse;

/**
 * Object used for UserClient Password resetting
 */
public class ResetPasswordRequest {

    private String resetToken;
    private String newPassword;
    private List<UserClientSecretQuestionResponse> userClientSecretQuestionResponse;

    public ResetPasswordRequest() {
    	 System.out.println("whrere arera e empty contr");
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

	public List<UserClientSecretQuestionResponse> getUserClientSecretQuestionResponse() {
		return userClientSecretQuestionResponse;
	}

	public void setUserClientSecretQuestionResponse(
			List<UserClientSecretQuestionResponse> userClientSecretQuestionResponse) {
		this.userClientSecretQuestionResponse = userClientSecretQuestionResponse;
	}

	
}
