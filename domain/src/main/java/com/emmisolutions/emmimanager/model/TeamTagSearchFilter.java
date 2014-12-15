package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * The search filter for TeamTag entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "filter")
public class TeamTagSearchFilter {

    private TeamTag teamTag;
    private Client client;
    private Set<Tag> tagSet;

    public TeamTagSearchFilter(TeamTag teamTag) {
        this.teamTag = teamTag;
    }
    public TeamTagSearchFilter() {
    }

    public TeamTag getTeamTag() {
        return teamTag;
    }

    public void setTeamTag(TeamTag teamTag) {
        this.teamTag = teamTag;
    }

    @Override
    public String toString() {
        return "TeamTagSearchFilter{" + "teamTags=" + tagSet + '}';
    }

    public Set<Tag> getTagSet() {
        return tagSet;
    }

    public void setTagSet(Set<Tag> tagSet) {
        this.tagSet = tagSet;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
