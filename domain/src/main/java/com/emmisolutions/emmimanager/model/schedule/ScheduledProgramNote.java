package com.emmisolutions.emmimanager.model.schedule;

public class ScheduledProgramNote {

    private String note;

    private ScheduledProgram scheduledProgram;

    public ScheduledProgram getScheduledProgram() {
        return scheduledProgram;
    }

    public void setScheduledProgram(ScheduledProgram scheduledProgram) {
        this.scheduledProgram = scheduledProgram;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "ScheduledProgramNote{" +
                "note='" + note + '\'' +
                ", scheduledProgram=" + scheduledProgram +
                '}';
    }
}
