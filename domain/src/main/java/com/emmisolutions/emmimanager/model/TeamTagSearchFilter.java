package com.emmisolutions.emmimanager.model;

import org.apache.commons.lang3.StringUtils;

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
    private StatusFilter status;
    private TeamTagType teamTagType;
    /**
     * Make a search filter for a particular team tag id
     */
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

    public StatusFilter getStatus() {
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public TeamTagType getTeamTagType() {
        return teamTagType;
    }

    public void setTeamTagType(TeamTagType teamTagType) {
        this.teamTagType = teamTagType;
    }

    /**
     * Team Statuses
     */
    public enum StatusFilter {
        ALL, ACTIVE_ONLY, INACTIVE_ONLY;

        /**
         * Create a StatusFilter from a string
         *
         * @param status if the string matches or ALL
         * @return StatusFilter
         */
        public static StatusFilter fromStringOrAll(String status) {
            if (StringUtils.isNotBlank(status)) {
                for (StatusFilter statusFilter : values()) {
                    if (statusFilter.toString().equals(status.toUpperCase())) {
                        return statusFilter;
                    }
                }
            }
            return ALL;
        }

        /**
         * Create a StatusFilter from a string
         *
         * @param status if the string matches or ACTIVE
         * @return StatusFilter
         */
        public static StatusFilter fromStringOrActive(String status) {
            if (StringUtils.isNotBlank(status)) {
                for (StatusFilter statusFilter : values()) {
                    if (statusFilter.toString().equals(status.toUpperCase())) {
                        return statusFilter;
                    }
                }
            }
            return ACTIVE_ONLY;
        }
    }

    /**
     * Type of TeamTag to search for
     */
    public enum TeamTagType {
        UNTAGGED_ONLY, TAGGED_ONLY, ALL;

        /**
         * Create a TeamTagType from a string
         *
         * @param type if the string matches or ALL
         * @return StatusFilter
         */
        public static TeamTagType fromStringOrAll(String type) {
            if (StringUtils.isNotBlank(type)) {
                for (TeamTagType teamTagType : values()) {
                    if (teamTagType.toString().equals(type.toUpperCase())) {
                        return teamTagType;
                    }
                }
            }
            return ALL;
        }
    }
}
