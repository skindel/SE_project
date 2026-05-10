package com.alerts;

import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodPressureAlerts extends AlertConditionChecker {
    
    @Override
    public void checkPatient(Patient patient, List<Alert> alerts) {
        List<PatientRecord> records = patient.getAllRecords();
        int patientId = patient.getPatientId();
        
        List<PatientRecord> pastThreeSystolic = pastNMeasurements("SystolicPressure", records, 3);
        List<PatientRecord> pastThreeDiastolic = pastNMeasurements("DiastolicPressure", records, 3);

        if(increasingTrend(pastThreeDiastolic) || increasingTrend(pastThreeSystolic)){
            Alert alert = new Alert(String.valueOf(patientId), "Increasing blood pressure trend", System.currentTimeMillis());
            alerts.add(alert);
        }

        if(decreasingTrend(pastThreeDiastolic) || decreasingTrend(pastThreeSystolic)){
            Alert alert = new Alert(String.valueOf(patientId), "Decreasing blood pressure trend", System.currentTimeMillis());
            alerts.add(alert);
        }

        if(criticalPressure(pastThreeSystolic.get(pastThreeSystolic.size()-1).getMeasurementValue(), pastThreeDiastolic.get(pastThreeDiastolic.size()-1).getMeasurementValue())){
            Alert alert = new Alert(String.valueOf(patientId), "Critical blood pressure reading", System.currentTimeMillis());
            alerts.add(alert);
        }
    }

    /**
     * Check for increasing trend in pressure - Verify an alert is triggered when three consecutive blood pressure readings increase by more than 10 mmHg each.
     * @param measurements - list of measurements
     * @return true if increasing trend detected, false otherwise
     */
    private boolean increasingTrend(List<PatientRecord> measurements){
        if(measurements.size() < 3){
            return false;
        }
        
        for(int i = 0; i < measurements.size()-1; i++){
            if(measurements.get(i).getMeasurementValue()+10 >= measurements.get(i+1).getMeasurementValue()){
                return false;
            }
        }
        return true;
    }
    /**
     * Check for decreasing trend in pressure - Verify an alert is triggered when three consecutive blood pressure readings increase by more than 10 mmHg each.
     * @param measurements  list of measurements
     * @return true if decreasing trend detected, false otherwise
     */
    private boolean decreasingTrend(List<PatientRecord> measurements){
        if(measurements.size() < 3){
            return false;
        }

        for(int i = 0; i < measurements.size()-1; i++){
            if(measurements.get(i).getMeasurementValue()-10 <= measurements.get(i+1).getMeasurementValue()){
                return false;
            }
        }
        return true;
    }

    /**
     * Check if blood pressure exceeds critical tresholds
     * @param systolic systolic pressure measurement
     * @param diastolic diastolic pressure measurement
     * @return true if critical pressure detected, false otherwise
     */
    private boolean criticalPressure(double systolic, double diastolic) {
        return systolic > 180 || diastolic > 120;
    }
}
