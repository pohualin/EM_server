package com.emmisolutions.emmimanager.web.rest.client.model.security;

import com.emmisolutions.emmimanager.model.user.client.UserClient;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class to hold the login failure reason. Also attach an UserClient and
 * ClientPasswordConfiguration to use in UI
 *
 */
@XmlRootElement(name = "user-client-login-failure")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientLoginError extends ResourceSupport {

    private Reason reason;

    private UserClient userClient;

    public UserClientLoginError() {
    }

    public UserClientLoginError(Reason reason) {
        this.reason = reason;
    }

    public UserClientLoginError(Reason reason, UserClient userClient) {
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
        UserClientLoginError that = (UserClientLoginError) o;
        return reason == that.reason
                && !(userClient != null ? !userClient.equals(that.userClient)
                        : that.userClient != null);
    }

    @Override
    public int hashCode() {
        int result = reason != null ? reason.hashCode() : 0;
        result = 31 * result + (userClient != null ? userClient.hashCode() : 0);
        return result;
    }

    @XmlEnum
    public enum Reason {
        BAD, LOCK, EXPIRED, EXPIRED_CANT_CHANGE, XSRF_MISSING, XSRF_INVALID
    }
}
