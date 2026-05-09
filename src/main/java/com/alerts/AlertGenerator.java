package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;

// changed comments
/**
 * Monitors patient data and generates alerts.
 */
public class AlertGenerator {

    //changed to camelcase, added final
    private final DataStorage dataStorage;

    /**
     * @param dataStorage data storage object/ssytem
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates a patient's data for alert conditions.
     *
     * @param patient the patient data to evaluate.
     */
    public void evaluateData(Patient patient) {
        // TODO: Implementation goes here
    }

    /**
     * Triggers an alert
     *
     * @param alert the alert details.
     */
    private void triggerAlert(Alert alert) {
        // TODO:Implementation might involve logging the alert or notifying staff
    }
}