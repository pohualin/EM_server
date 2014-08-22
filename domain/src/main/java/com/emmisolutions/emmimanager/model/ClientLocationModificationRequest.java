package com.emmisolutions.emmimanager.model;

import java.util.List;

/**
 * A request to modify the locations attached to a client
 */
public class ClientLocationModificationRequest {

    private List<Location> added;

    private List<Location> deleted;

    private List<Location> updated;

    public List<Location> getAdded() {
        return added;
    }

    public void setAdded(List<Location> added) {
        this.added = added;
    }

    public List<Location> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<Location> deleted) {
        this.deleted = deleted;
    }

    public List<Location> getUpdated() {
        return updated;
    }

    public void setUpdated(List<Location> updated) {
        this.updated = updated;
    }
}
