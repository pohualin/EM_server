package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The search filter for TeamLocation entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "filter")
public class TeamLocationSearchFilter {

    private Long teamLocationId;

    /**
     * Constructor
     *
     * @param teamLocationId for a location id
     */
    public TeamLocationSearchFilter(Long teamLocationId) {
        this.teamLocationId = teamLocationId;
    }

    public Long getTeamTagId() {
        return teamLocationId;
    }

    public void setTeamTagId(Long teamLocationId) {
        this.teamLocationId = teamLocationId;
    }

    @Override
    public String toString() {
        return "TeamLocationSearchFilter{" + "teamLocationId=" + teamLocationId + '}';
    }
}
