package com.testAll;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alerts.EcgAlerts;
import com.alerts.Alert;
import com.data_management.Patient;

public class EcgAlertsTests {

    @Test
    public void aboveMeanDetectsAbnormal() {
        Patient p = new Patient(7);
        long base = System.currentTimeMillis();
        // 19 normal values
        for (int i = 0; i < 19; i++) {
            p.addRecord(1.0, "ECG", base - (2000 - i * 10));
        }
        // last abnormal value
        p.addRecord(5.0, "ECG", base);

        List<Alert> alerts = new ArrayList<>();
        new EcgAlerts().checkPatient(p, alerts, System.currentTimeMillis());

        assertTrue(alerts.stream().anyMatch(a -> a.getCondition().toLowerCase().contains("ecg")), "Expected ECG abnormal alert");
    }
}
