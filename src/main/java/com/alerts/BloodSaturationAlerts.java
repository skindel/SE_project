package com.alerts;

import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodSaturationAlerts extends AlertConditionChecker {
    @Override
    public void checkPatient(Patient patient, List<Alert> alerts, long timestamp) {
        List<PatientRecord> records = patient.getRecords(System.currentTimeMillis() - 600000, System.currentTimeMillis());
        List<Double> saturationLevels = getMeasurements("Saturation", records, timestamp);
        int patientId = patient.getPatientId();

        if(saturationLevels.get(0)<92){
            alerts.add(
                new Alert(String.valueOf(patientId), "Low blood saturation", System.currentTimeMillis())
            );
        }

        if(rapidDrop(saturationLevels)){
            alerts.add(
                new Alert(String.valueOf(patientId), "Rapid drop in blood saturation", System.currentTimeMillis())
            );
        }
    }

    /**
     * Checks for rapid drop in saturation levels greater than 5%
     * @param saturationLevels list of saturation levels on the period that needs to b checked
     * @return true if rapid drop detected, false otherwise
     */
    private boolean rapidDrop(List<Double> saturationLevels) {
        double treshold = 5.0;
        double max = 0;
        for(int i = 0; i < saturationLevels.size(); i++){
            if(saturationLevels.get(i) > max){
                max = saturationLevels.get(i);
                continue;
            }

            if(max - saturationLevels.get(i) > treshold){
                return true;
            }
        }

        return false;
    }

}
