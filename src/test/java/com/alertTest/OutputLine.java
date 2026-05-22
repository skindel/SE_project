package com.alertTest;

public class OutputLine {
    private final int patientId;
    private final String condition;
    private final long timestamp;

    public OutputLine(int patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getCondition() {
        return condition;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
