package com.alerts.Factory.AlertSubtypes;

import com.alerts.Alert;

public class ECGAlert extends Alert {
    public ECGAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }    
}
