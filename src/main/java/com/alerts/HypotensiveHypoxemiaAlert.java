package com.alerts;

import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * checks for hypotensive hypoxemia condition
 */
public class HypotensiveHypoxemiaAlert extends AlertConditionChecker {
    @Override
    public void checkPatient(Patient patient, List<Alert> alerts, long timestamp){
        List<PatientRecord> records = patient.getAllRecords();
        
        double latestSystolic = pastNMeasurements("Systolic", records, 1,timestamp).get(0).getMeasurementValue();
        double latestSaturation = pastNMeasurements("BloodSaturation", records, 1, timestamp).get(0).getMeasurementValue();

        int patientId = patient.getPatientId();

        if(latestSystolic < 90 && latestSaturation < 92){
            Alert alert = new Alert(String.valueOf(patientId), "Hypotensive hypoxemia detected", System.currentTimeMillis());
            alerts.add(alert);
        }
    }
}
