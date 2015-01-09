package com.emmisolutions.emmimanager.model;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter used for Team searching.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamSearchFilter {
    @XmlElement(name = "name")
    @XmlElementWrapper(name = "names")
    private Set<String> names;

    private Tag tag;

    private StatusFilter status;

    private Long clientId;

    private TeamTagType teamTagType;

    /**
     * Constructor
     */
    public TeamSearchFilter() {
        status = StatusFilter.ALL;
    }

    /**
     * Constructor
     *
     * @param names to filter
     */
    public TeamSearchFilter(String... names) {
        this(StatusFilter.ALL, names);
    }

    /**
     * Constructor
     *
     * @param teamTagType type of teamtags to filter
     */
    public TeamSearchFilter(TeamTagType teamTagType) {
        this(StatusFilter.ALL);
        if (teamTagType != null) {
            this.teamTagType = teamTagType;
        }
    }

    /**
     * Constructor
     *
     * @param status      filter
     * @param teamTagType type of teamtags to filter
     */
    public TeamSearchFilter(StatusFilter status, TeamTagType teamTagType) {
        if (status != null) {
            this.status = status;
        }
        if (teamTagType != null) {
            this.teamTagType = teamTagType;
        }
    }

    /**
     * Constructor
     *
     * @param teamTagType type of teamtags to filter
     */
    public TeamSearchFilter(Long clientId, TeamTagType teamTagType) {
        this(teamTagType);
        this.clientId = clientId;
    }

    /**
     * Constructor
     *
     * @param status      filter
     * @param teamTagType type of teamtags to filter
     */
    public TeamSearchFilter(Long clientId, StatusFilter status, TeamTagType teamTagType) {
        this(status, teamTagType);
        this.clientId = clientId;
    }

    /**
     * Constructor
     *
     * @param status      filter
     * @param teamTagType type of teamtags to filter
     */
    public TeamSearchFilter(Long clientId, StatusFilter status, TeamTagType teamTagType, String... names) {
        this(clientId, status, teamTagType);
        if (names != null) {
            this.names = new HashSet<>();
            Collections.addAll(this.names, names);
        }
    }

    /**
     * Constructor
     *
     * @param status filter
     * @param names  to filter
     */
    public TeamSearchFilter(StatusFilter status, String... names) {
        if (names != null) {
            this.names = new HashSet<>();
            Collections.addAll(this.names, names);
        }
        if (status != null) {
            this.status = status;
        }
    }

    /**
     * Constructor
     *
     * @param status filter
     * @param names  filter
     */
    public TeamSearchFilter(StatusFilter status, String names) {
        if (names != null) {
            this.names = new HashSet<>();
            Collections.addAll(this.names, names);
        }
        if (status != null) {
            this.status = status;
        }
    }

    /**
     * Constructor
     *
     * @param clientId the client filter
     * @param status   filter
     * @param names    filters
     */
    public TeamSearchFilter(Long clientId, StatusFilter status, String... names) {
        this(status, names);
        this.clientId = clientId;
    }

    public Set<String> getNames() {
        return names;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public Long getClientId() {
        return clientId;
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
        ALL;

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

    @Override
    public String toString() {
        return "TeamSearchFilter{" + "names=" + names + ", status=" + status
                + '}';
    }
}
