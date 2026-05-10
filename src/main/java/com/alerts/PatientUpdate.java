package com.alerts;

public class PatientUpdate {
    private int patientId;
    private String measurementType;

    public PatientUpdate(int patientId, String measurementType){
        this.patientId = patientId;
        this.measurementType = measurementType;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getMeasurementType() {
        return measurementType;
    }
}
