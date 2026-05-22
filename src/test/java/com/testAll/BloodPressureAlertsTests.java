package com.testAll;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alerts.BloodPressureAlerts;
import com.alerts.Alert;
import com.data_management.Patient;

public class BloodPressureAlertsTests {

    @Test
    public void increasingTrendProducesAlert() {
        Patient p = new Patient(5);
        long base = System.currentTimeMillis();
        // three increasing systolic readings >10 apart
        p.addRecord(100.0, "SystolicPressure", base - 3000);
        p.addRecord(111.0, "SystolicPressure", base - 2000);
        p.addRecord(122.0, "SystolicPressure", base - 1000);
        // diastolic placeholder
        p.addRecord(70.0, "DiastolicPressure", base - 1000);

        List<Alert> alerts = new ArrayList<>();
        new BloodPressureAlerts().checkPatient(p, alerts, System.currentTimeMillis());

        assertTrue(alerts.stream().anyMatch(a -> a.getCondition().toLowerCase().contains("increasing")), "Expected increasing trend alert");
    }

    @Test
    public void criticalPressureProducesAlert() {
        Patient p = new Patient(6);
        long base = System.currentTimeMillis();
        p.addRecord(190.0, "SystolicPressure", base - 1000);
        p.addRecord(121.0, "DiastolicPressure", base - 1000);

        List<Alert> alerts = new ArrayList<>();
        new BloodPressureAlerts().checkPatient(p, alerts, System.currentTimeMillis());

        assertTrue(alerts.stream().anyMatch(a -> a.getCondition().toLowerCase().contains("critical")), "Expected critical blood pressure alert");
    }
}
