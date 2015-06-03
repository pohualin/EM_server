package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * The search filter for UserClient entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserClientSearchFilter extends UserClientCommonSearchFilter {

    private Client client;

    private Team team;

    private Tag tag;

    /**
     * constructor
     */
    public UserClientSearchFilter() {
        super();
    }

    /**
     * all status plus passed term
     */
    public UserClientSearchFilter(Client client, String term) {
       super(term);
       this.client = client;
    }

    /**
     * constructor
     *
     * @param status to filter
     * @param term   to filter
     */
    public UserClientSearchFilter(Client client, StatusFilter status,
                                  String term) {
        super(status, term);
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Tag getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "UserClientSearchFilter{" + "term=" + getTerm() + ", status="
                + getStatus() + '}';
    }
}
