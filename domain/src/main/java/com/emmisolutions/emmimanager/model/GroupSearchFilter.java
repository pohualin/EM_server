package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The search filter for Group entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "filter")
public class GroupSearchFilter {

    private Long clientId;

    /**
     * constructor
     *
     * @param clientId the client id
     */
    public GroupSearchFilter(Long clientId) {
        this.clientId = clientId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "GroupSearchFilter{" + "clientID=" + clientId + '}';
    }
}
