package com.alerts.Factory;

import java.util.List;

import com.alerts.Alert;

/**
 * Creates a list of alerts (possible multiple alerts per one log)
 * 
 * my code's idea of implementation is (was) way different so it might seem unnatural in some places
 */
public abstract class AlertFactory {
    public abstract Alert generateAlert(String patientId, String condition, long timestamp);
}
