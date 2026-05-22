package com.data_management;

public class PatientUpdate {
    private Patient patient;
    private String measurementType;
    private long timestamp;

    public PatientUpdate(Patient patient, String measurementType, long timestamp){
        this.patient = patient;
        this.measurementType = measurementType;
        this.timestamp = timestamp;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getMeasurementType() {
        return measurementType;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
