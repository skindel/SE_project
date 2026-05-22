package com.alerts;

import java.util.ArrayList;
import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public abstract class AlertConditionChecker {
    // returns list of alerts for a patient
    public abstract void checkPatient(Patient patient, List<Alert> alerts, long timestamp);

    private List<PatientRecord> entriesTillTimestamp(List<PatientRecord> records, long timestamp){
        List<PatientRecord> recTimestamp = new ArrayList<>();
        for(int i = 0; i < records.size(); i++){
            if(records.get(i).getTimestamp()<=timestamp){
                recTimestamp.add(records.get(i));
            } else {
                break;
            }
        }
        return recTimestamp;
    }
    /**
     * Helper method to get measurements of a specific type
     * @param type type of measurement
     * @param records lsit of patient records to check
     * @return values of required measurement
     */
    protected List<Double> getMeasurements(String type, List<PatientRecord> records, long timestamp){
        records = entriesTillTimestamp(records, timestamp);
        List<Double> measurements = new ArrayList<>();
        for(PatientRecord r : records){
            if(type.equals(r.getRecordType())){
                measurements.add(r.getMeasurementValue());
            }
        }
        return measurements;
    }

    /**
     * Helper method to get last n measurements of a specifiv type
     * @param type type of measurement
     * @param records list of patient records to check
     * @param n number of measurements to get
     * @return list of values for measurement
     */
    protected List<PatientRecord> pastNMeasurements(String type, List<PatientRecord> records, int n, long timestamp){
        records = entriesTillTimestamp(records, timestamp);
        List<PatientRecord> measurements = new ArrayList<>();
        for(int i = records.size() - 1; i >= 0 && measurements.size() < n; i--){
            if(type.equals(records.get(i).getRecordType())){
                measurements.add(records.get(i));
            }
        }
        // currently collected newest->oldest, reverse to oldest->newest
        List<PatientRecord> ordered = new ArrayList<>();
        for(int i = measurements.size() - 1; i >= 0; i--){
            ordered.add(measurements.get(i));
        }
        return ordered;
    }
}