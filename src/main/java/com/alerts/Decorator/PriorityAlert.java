package com.alerts.Decorator;

import com.alerts.Alert;
import com.alerts.AlertManager;
public class PriorityAlert extends AlertDecorator {
    private int priorityLevel = 0; 

    public PriorityAlert(Alert alert, AlertManager manager) {
        super(alert, manager);
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }
    
}
