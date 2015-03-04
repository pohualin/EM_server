package com.emmisolutions.emmimanager.model;

import java.util.List;

/**
 * A request for Reference Group and its associated reference tags
 *
 */
public class RefGroupSaveRequest {

	private ReferenceGroup referenceGroup;

	private List<ReferenceTag> referenceTags;

    public ReferenceGroup getReferenceGroup() {
        return referenceGroup;
    }

    public void setReferenceGroup(ReferenceGroup referenceGroup) {
        this.referenceGroup = referenceGroup;
    }

    public List<ReferenceTag> getReferenceTags() {
        return referenceTags;
    }

    public void setReferenceTags(List<ReferenceTag> referenceTags) {
        this.referenceTags = referenceTags;
    }

    @Override
    public String toString() {
        return "RefGroupSaveRequest [referenceGroup=" + referenceGroup
                + ", referenceTags=" + referenceTags + "]";
    }

}
