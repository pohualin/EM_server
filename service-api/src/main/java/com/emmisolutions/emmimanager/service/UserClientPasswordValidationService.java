package com.emmisolutions.emmimanager.service;

import java.util.List;

import javax.xml.bind.annotation.XmlEnum;

import com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.password.ChangePasswordRequest;

/**
 * Password validation service for an UserClient
 */
public interface UserClientPasswordValidationService {

    /**
     * Verify existing password and validate new password pattern
     * 
     * @param changePasswordRequest
     *            to check
     * @return a list of UserClientPasswordValidationError with reason
     */
    List<UserClientPasswordValidationError> validateRequest(
            ChangePasswordRequest changePasswordRequest);

    /**
     * Check and see if password meet the password policy pattern
     * 
     * @param userClient
     *            to check
     * @return null if meet or UserClientPasswordValidationError with reason
     *         POLICY
     */
    UserClientPasswordValidationError validatePasswordPattern(
            UserClient userClient);
    
    UserClientPasswordValidationError checkPasswordHistory(UserClient userClient);

    /**
     * Check if password matches client password policy pattern
     * 
     * @param confituration
     *            to refer
     * @param password
     *            to valicate
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
        BAD, POLICY, HISTORY
    }
}
