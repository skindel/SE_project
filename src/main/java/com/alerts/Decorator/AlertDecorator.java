package com.alerts.Decorator;

import com.alerts.Alert;
import com.alerts.AlertManager;

public class AlertDecorator extends Alert {
    protected Alert alert;
    protected AlertManager manager;
    
    public AlertDecorator(Alert alert, AlertManager alertManager) {
        super(alert.getPatientId(), alert.getCondition(), alert.getTimestamp());
        this.alert = alert;
        this.manager = manager;
    }
}
