package com.alerts.Decorator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.alerts.Alert;
import com.alerts.AlertManager;

public class RepeatAlert extends AlertDecorator {
    private final ScheduledExecutorService scheduler;
    private final int repeatIntervalSeconds = 10; // Repeat every 10 seconds


    public RepeatAlert(Alert decoratedAlert, AlertManager manager) {
        super(decoratedAlert, manager);

        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> check(), 0, repeatIntervalSeconds, TimeUnit.SECONDS);
    }

    private void check(){
        // this is clunky but works, my architecture is not made for this
        manager.evaluatePatientParameter(manager.getStorage().getPatient(Integer.parseInt(patientId)), condition, System.currentTimeMillis());
    }
}
