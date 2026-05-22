package com.alerts;

import java.nio.channels.Pipe.SourceChannel;
import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class TriggeredAlerts extends AlertConditionChecker {
    @Override
    public void checkPatient(Patient patient, List<Alert> alerts, long timestamp) {
        List<PatientRecord> records = patient.getAllRecords();
        PatientRecord record = pastNMeasurements("Alert", records, 1, timestamp).get(0);
        if(record.getMeasurementValue()==1){
            alerts.add(new Alert(String.valueOf(record.getPatientId()),"Triggered Alert", record.getTimestamp()));
        }
    }
}
