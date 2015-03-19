package com.emmisolutions.emmimanager.service;

import java.util.List;

import javax.xml.bind.annotation.XmlEnum;

import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.activation.ActivationRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ChangePasswordRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ExpiredPasswordChangeRequest;
import com.emmisolutions.emmimanager.model.user.client.password.ResetPasswordRequest;

/**
 * Password validation service for an UserClient
 */
public interface UserClientPasswordValidationService {

    /**
     * Validate new password passed in ChangePasswordRequest. Must meet password
     * policy and not repeat X numbers of past passwords
     * 
     * @param changePasswordRequest
     *            to check
     * @return a list of UserClientPasswordValidationError with reason
     */
    List<UserClientPasswordValidationError> validateRequest(
            ChangePasswordRequest changePasswordRequest);

    /**
     * Validate new password passed in ExpiredPasswordChangeRequest. Must meet
     * password policy and not repeat X numbers of past passwords
     * 
     * @param expiredPasswordChangeRequest
     *            to check
     * @return a list of UserClientPasswordValidationError with reason
     */
    List<UserClientPasswordValidationError> validateRequest(
            ExpiredPasswordChangeRequest expiredPasswordChangeRequest);

    /**
     * Validate new password passed in ResetPasswordRequest. Must meet password
     * policy and not repeat X numbers of past passwords
     * 
     * @param resetPasswordRequest
     *            to check
     * @return a list of UserClientPasswordValidationError with reason
     */
    List<UserClientPasswordValidationError> validateRequest(
            ResetPasswordRequest resetPasswordRequest);

    /**
     * Validate new password passed in ActivationRequest. Must meet password
     * policy and not repeat X numbers of past passwords
     * 
     * @param activationRequest
     *            to check
     * @return a list of UserClientPasswordValidationError with reason
     */
    List<UserClientPasswordValidationError> validateRequest(
            ActivationRequest activationRequest);

    /**
     * See if password repeats past x passwords
     * 
     * @param confituration
     *            to refer
     * @param existing
     *            to use
     * @param password
     *            to check
     * @return true if password is valid, false if not
     */
    boolean checkPasswordHistory(ClientPasswordConfiguration confituration,
            UserClient existing, String password);

    /**
     * Check if password matches client password policy pattern
     * 
     * @param confituration
     *            to refer
     * @param password
     *            to validate
     * @return true if match or false if not
     */
    boolean validatePasswordPattern(ClientPasswordConfiguration confituration,
            String password);

    public class UserClientPasswordValidationError {
        private Reason reason;

        public UserClientPasswordValidationError() {
        }

        public UserClientPasswordValidationError(Reason reason) {
            this.reason = reason;
        }

        public Reason getReason() {
            return reason;
        }

        public void setReason(Reason reason) {
            this.reason = reason;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            UserClientPasswordValidationError that = (UserClientPasswordValidationError) o;
            return reason == that.reason;
        }

        @Override
        public int hashCode() {
            int result = reason != null ? reason.hashCode() : 0;
            result = 31 * result;
            return result;
        }
    }

    @XmlEnum
    public static enum Reason {
        POLICY, HISTORY
    }
}
