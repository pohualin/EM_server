package com.emmisolutions.emmimanager.service;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.model.user.client.activation.ActivationRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.xml.bind.annotation.XmlEnum;

import java.util.List;

/**
 * The UserClient Service
 */
public interface UserClientService {

    /**
     * Creates a new UserClient with the passed UserClient object.
     *
     * @param user to create
     * @return the created user
     */
    UserClient create(UserClient user);

    /**
     * Reload an existing client user with the passed userClientId.
     *
     * @param userClient to use
     * @return the existing UserClient
     */
    UserClient reload(UserClient userClient);

    /**
     * Update an existing UserClient with the passed in UserClient object.
     *
     * @param user to update
     * @return the created user
     */
    UserClient update(UserClient user);

    /**
     * Find existing UserClient with the passed in UserClientSearchFilter.
     *
     * @param filter to search
     * @return pageable UserClient
     */
    Page<UserClient> list(Pageable pageable, UserClientSearchFilter filter);

    /**
     * Find an existing UserClient objects that would conflict with the saving
     * of the passed user client
     *
     * @param userClient to check for conflicts
     * @return a list of conflicts
     */
    List<UserClientConflict> findConflictingUsers(UserClient userClient);

    /**
     * Activate a user from an activation code
     *
     * @param activationRequest used to activate the user
     * @return the activated UserClient
     */
    UserClient activate(ActivationRequest activationRequest);

    /**
     * Adds an activation key to the passed user client
     *
     * @param userClient to add the key to
     * @return the updated UserClient
     */
    UserClient addActivationKey(UserClient userClient);

    /**
     * Expires the activation token but does not remove it
     *
     * @param userClient on which to expire the token
     * @return the updated UserClient
     */
    UserClient expireActivationToken(UserClient userClient);
    
    /**
     * return true if the email is valid
     * 
     * @param userClient contains email to validate
     * @return boolean
     */
    boolean validateEmailAddress(UserClient userClient);

    /**
     * Number of hours activation tokens are valid after creation
     */
    int ACTIVATION_TOKEN_HOURS_VALID = 72;

    /**
     * A conflicting UserClient
     */
    public class UserClientConflict {
        private Reason reason;
        private UserClient userClient;

        public UserClientConflict() {
        }

        public UserClientConflict(Reason reason, UserClient userClient) {
            this.reason = reason;
            this.userClient = userClient;
        }

        public Reason getReason() {
            return reason;
        }

        public void setReason(Reason reason) {
            this.reason = reason;
        }

        public UserClient getUserClient() {
            return userClient;
        }

        public void setUserClient(UserClient userClient) {
            this.userClient = userClient;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            UserClientConflict that = (UserClientConflict) o;
            return reason == that.reason
                    && !(userClient != null ? !userClient
                    .equals(that.userClient) : that.userClient != null);
        }

        @Override
        public int hashCode() {
            int result = reason != null ? reason.hashCode() : 0;
            result = 31 * result
                    + (userClient != null ? userClient.hashCode() : 0);
            return result;
        }
    }
    
    public class UserClientValidationError {
        private Reason reason;
        private UserClient userClient;

        public UserClientValidationError() {
        }

        public UserClientValidationError(Reason reason, UserClient userClient) {
            this.reason = reason;
            this.userClient = userClient;
        }

        public Reason getReason() {
            return reason;
        }

        public void setReason(Reason reason) {
            this.reason = reason;
        }

        public UserClient getUserClient() {
            return userClient;
        }

        public void setUserClient(UserClient userClient) {
            this.userClient = userClient;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            UserClientConflict that = (UserClientConflict) o;
            return reason == that.reason
                    && !(userClient != null ? !userClient
                    .equals(that.userClient) : that.userClient != null);
        }

        @Override
        public int hashCode() {
            int result = reason != null ? reason.hashCode() : 0;
            result = 31 * result
                    + (userClient != null ? userClient.hashCode() : 0);
            return result;
        }
    }
    
    @XmlEnum
    public enum Reason {
        EMAIL, LOGIN, EMAIL_RESTRICTION
    }
}
