package com.alerts.Factory;

import com.alerts.Alert;
import com.alerts.Factory.AlertSubtypes.ECGAlert;

public class ECGAlertFactory extends AlertFactory {
    @Override
    public Alert generateAlert(String patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
    
}
