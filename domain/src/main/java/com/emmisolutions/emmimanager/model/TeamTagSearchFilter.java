package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The search filter for TeamTag entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "filter")
public class TeamTagSearchFilter {

    private Long teamTagId;
    private Long tagId;

    /**
     * Make a search filter for a particular team tag id
     *
     * @param teamTagId to use
     */
    public TeamTagSearchFilter(Long teamTagId) {
        this.teamTagId = teamTagId;
    }

    public TeamTagSearchFilter() {
    }

    public Long getTeamTagId() {
        return teamTagId;
    }

    public void setTeamTagId(Long teamTagId) {
        this.teamTagId = teamTagId;
    }

    @Override
    public String toString() {
        return "TeamTagSearchFilter{" + "teamTagId=" + teamTagId + '}';
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
