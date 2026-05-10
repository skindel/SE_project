package com.alerts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.data_management.DataStorage;
import com.data_management.Patient;

// changed comments
/**
 * Monitors patient data and generates alerts.
 */
public class AlertManager {
    // changed to camelcase, added final
    private final DataStorage dataStorage;
    private final ScheduledExecutorService scheduler;

    // queue for checking updates in patient data and generating alerts - concurrent so that it can work with multiple threads
    private ConcurrentLinkedQueue<PatientUpdate> updateQueue = new ConcurrentLinkedQueue<>();

    private final Map<String, List<AlertConditionChecker>> alertCheckers = new HashMap<>(Map.of(
        "BloodSaturation", List.of(new BloodSaturationAlerts(), new HypotensiveHypoxemiaAlert()),
        "ECG", List.of(new EcgAlerts()),
        "HypotensiveHypoxemia", List.of(new HypotensiveHypoxemiaAlert()),
        "SystolicPressure", List.of(new HypotensiveHypoxemiaAlert(), new BloodPressureAlerts()),
        "DiastolicPressure", List.of(new BloodPressureAlerts()),
        "Alert", List.of(new TriggeredAlerts())
    ));
    
    /**
     * @param dataStorage data storage object/system
     */
    public AlertManager(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> processUpdates(), 0, 200, TimeUnit.MILLISECONDS);
    }

    public void addPatientUpdate(PatientUpdate update){
        updateQueue.add(update);
    }

    /**
     * Provess all queued updates and parses to evaluate for alerts.
     */
    private void processUpdates(){
        while(!updateQueue.isEmpty()){
            PatientUpdate update = updateQueue.poll();
            if(update != null){
                Patient patient = dataStorage.getPatient(update.getPatientId());
                evaluatePatientParameter(patient, update.getMeasurementType());
            }
        }
    }

    /**
     * Evaluates patient data against all relevant alert conditions and triggers those alerts.
     *
     * @param patient the patient data to evaluate.
     * @param recordType the type of record to evaluate.
     */
    public void evaluatePatientParameter(Patient patient, String recordType) {
        List<Alert> alerts = new ArrayList<>();
        if(alertCheckers.containsKey(recordType)){
            for(AlertConditionChecker checker : alertCheckers.get(recordType)){
                checker.checkPatient(patient, alerts);
            }
        }
        for(Alert alert : alerts){
            triggerAlert(alert);
        }
    }

    /**
     * Triggers an alert
     *
     * @param alert the alert details.
     */
    private void triggerAlert(Alert alert) {
        System.out.println("ALERT: " + alert.getCondition() + " for patient " + alert.getPatientId() + " at " + alert.getTimestamp());
    }
}