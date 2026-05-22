package com.alerts.Factory;

import com.alerts.Alert;
import com.alerts.Factory.AlertSubtypes.BloodPressureAlert;

public class BloodPressureAlertFactory extends AlertFactory {
    @Override
    public Alert generateAlert(String patientId, String condition, long timestamp) {

        return new BloodPressureAlert(patientId, condition, timestamp);

    }
}
