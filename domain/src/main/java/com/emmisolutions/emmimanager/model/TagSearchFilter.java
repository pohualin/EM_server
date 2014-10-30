package com.emmisolutions.emmimanager.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The search filter for Tag entities
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "filter")
public class TagSearchFilter {

    private Long groupId;

    /**
     * Filter based upon group id
     *
     * @param groupId group id to filter by
     */
    public TagSearchFilter(Long groupId) {
        this.groupId = groupId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "TagSearchFilter{" + "groupId=" + groupId + '}';
    }

}
