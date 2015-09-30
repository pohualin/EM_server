package com.emmisolutions.emmimanager.model.schedule.remote;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramNotesJson implements Serializable {

    private int sequenceNumber;

    private String notes;

    private String viewId;

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    @Override
    public String toString() {
        return "ProgramNotesJson{" +
                "sequenceNumber=" + sequenceNumber +
                ", notes='" + notes + '\'' +
                ", viewId='" + viewId + '\'' +
                '}';
    }

}
