package com.testAll;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alerts.HypotensiveHypoxemiaAlert;
import com.alerts.Alert;
import com.data_management.Patient;

public class HypotensiveHypoxemiaTests {

    @Test
    public void detectsHypotensiveHypoxemia() {
        Patient p = new Patient(8);
        long base = System.currentTimeMillis();
        // Note: HypotensiveHypoxemiaAlert expects types "Systolic" and "BloodSaturation"
        p.addRecord(85.0, "Systolic", base - 1000);
        p.addRecord(90.0, "BloodSaturation", base - 500); // 90 < 92

        List<Alert> alerts = new ArrayList<>();
        new HypotensiveHypoxemiaAlert().checkPatient(p, alerts, System.currentTimeMillis());

        assertTrue(alerts.stream().anyMatch(a -> a.getCondition().toLowerCase().contains("hypotensive")), "Expected hypotensive hypoxemia alert");
    }
}
