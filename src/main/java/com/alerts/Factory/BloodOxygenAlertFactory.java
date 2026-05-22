package com.alerts.Factory;

import com.alerts.Alert;
import com.alerts.Factory.AlertSubtypes.BloodOxygenAlert;
public class BloodOxygenAlertFactory extends AlertFactory {
    @Override
    public Alert generateAlert(String patientId, String condition, long timestamp) {
        return new BloodOxygenAlert(patientId, condition, timestamp);
    }
}
