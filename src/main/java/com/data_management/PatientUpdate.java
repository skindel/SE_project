package com.data_management;

public class PatientUpdate {
    private Patient patient;
    private String measurementType;

    public PatientUpdate(Patient patient, String measurementType){
        this.patient = patient;
        this.measurementType = measurementType;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getMeasurementType() {
        return measurementType;
    }
}
