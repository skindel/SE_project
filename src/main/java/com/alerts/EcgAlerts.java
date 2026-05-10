package com.alerts;

import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * checks for abnormal ECG peaks - values above mean by at least treshold value
 */
public class EcgAlerts extends AlertConditionChecker {
    private final int windowSize = 20;
    private final double treshold = 1;

    @Override
    public void checkPatient(Patient patient, List<Alert> alerts) {
        List<PatientRecord> records = patient.getAllRecords();
        int patientId = patient.getPatientId();
        
        List<PatientRecord> pastEcgValues = pastNMeasurements("ECG", records, windowSize);
        
        if(aboveMean(pastEcgValues)){
            Alert alert = new Alert(String.valueOf(patientId), "Abnormal ECG pattern", System.currentTimeMillis());
            alerts.add(alert);
        }
    }

    /**
     * checks if latest value is above mean of preious 20 measurements by more than treshold percentage
     * @param values list of values
     * @return true if latest value is above mean by more than treshold value, false otherwise
     */
    public boolean aboveMean(List<PatientRecord> values){
        if(values.size() < windowSize){
            return false;
        }
        
        double sum = 0;
        for(PatientRecord v : values){
            sum += v.getMeasurementValue();
        }
        double mean = sum / values.size();
        double newest = values.get(values.size() - 1).getMeasurementValue();
        return Math.abs(mean - newest) > treshold;
    }
}