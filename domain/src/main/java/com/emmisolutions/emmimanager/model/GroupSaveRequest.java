package com.emmisolutions.emmimanager.model;

import java.util.List;

/**
 * A request for Group and its associated tags
 *
 */
public class GroupSaveRequest {

	private Group group;

	private List<Tag> tags;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

}
