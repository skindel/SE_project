package com.testAll;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alerts.TriggeredAlerts;
import com.alerts.Alert;
import com.data_management.Patient;

public class TriggeredAlertsTests {

    @Test
    public void triggeredAlertWhenValueIsOne() {
        Patient p = new Patient(9);
        long base = System.currentTimeMillis();
        p.addRecord(1.0, "Alert", base);

        List<Alert> alerts = new ArrayList<>();
        new TriggeredAlerts().checkPatient(p, alerts, System.currentTimeMillis());

        assertTrue(alerts.stream().anyMatch(a -> a.getCondition().toLowerCase().contains("triggered")), "Expected triggered alert");
    }
}
