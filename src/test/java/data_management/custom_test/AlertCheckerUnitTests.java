package data_management.custom_test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alerts.BloodPressureAlerts;
import com.alerts.BloodSaturationAlerts;
import com.alerts.EcgAlerts;
import com.alerts.HypotensiveHypoxemiaAlert;
import com.alerts.TriggeredAlerts;
import com.data_management.DataStorage;
import com.data_management.Patient;

public class AlertCheckerUnitTests {

    @Test
    public void triggeredAlertDetected() {
        DataStorage storage = new DataStorage();
        long now = System.currentTimeMillis();
        TestUtils.addRecord(storage, 999, 1.0, "Alert", now);
        Patient p = storage.getPatient(999);
        List<com.alerts.Alert> alerts = new ArrayList<>();
        new TriggeredAlerts().checkPatient(p, alerts);
        assertFalse(alerts.isEmpty(), "TriggeredAlerts should report an alert for value 1.0");
    }

    @Test
    public void ecgAboveMeanTriggers() {
        DataStorage storage = new DataStorage();
        int pid = 1000;
        long now = System.currentTimeMillis();
        // 19 small values
        for (int i = 0; i < 19; i++) {
            TestUtils.addRecord(storage, pid, 0.1 * i, "ECG", now + i);
        }
        // large spike
        TestUtils.addRecord(storage, pid, 10.0, "ECG", now + 19);
        Patient p = storage.getPatient(pid);
        List<com.alerts.Alert> alerts = new ArrayList<>();
        new EcgAlerts().checkPatient(p, alerts);
        assertFalse(alerts.isEmpty(), "EcgAlerts should detect abnormal ECG pattern");
    }

    @Test
    public void bloodPressureIncreasingAndCritical() {
        DataStorage storage = new DataStorage();
        int pid = 2000;
        long now = System.currentTimeMillis();
        // systolic increasing >10
        TestUtils.addRecord(storage, pid, 100, "SystolicPressure", now);
        TestUtils.addRecord(storage, pid, 111, "SystolicPressure", now + 1);
        TestUtils.addRecord(storage, pid, 122, "SystolicPressure", now + 2);

        // diastolic increasing too
        TestUtils.addRecord(storage, pid, 70, "DiastolicPressure", now);
        TestUtils.addRecord(storage, pid, 82, "DiastolicPressure", now + 1);
        TestUtils.addRecord(storage, pid, 95, "DiastolicPressure", now + 2);

        Patient p = storage.getPatient(pid);
        List<com.alerts.Alert> alerts = new ArrayList<>();
        new BloodPressureAlerts().checkPatient(p, alerts);
        assertFalse(alerts.isEmpty(), "BloodPressureAlerts should detect trend/critical conditions");

        // critical pressure
        DataStorage storage2 = new DataStorage();
        int pid2 = 2001;
        TestUtils.addRecord(storage2, pid2, 200, "SystolicPressure", now);
        TestUtils.addRecord(storage2, pid2, 130, "DiastolicPressure", now);
        Patient p2 = storage2.getPatient(pid2);
        List<com.alerts.Alert> alerts2 = new ArrayList<>();
        new BloodPressureAlerts().checkPatient(p2, alerts2);
        assertFalse(alerts2.isEmpty(), "BloodPressureAlerts should report critical pressure");
    }

    @Test
    public void bloodSaturationLowAndRapidDrop() {
        DataStorage storage = new DataStorage();
        int pid = 3000;
        long now = System.currentTimeMillis();
        // recent values within 10 minutes window
        TestUtils.addRecord(storage, pid, 90.0, "Saturation", now - 1000);
        TestUtils.addRecord(storage, pid, 96.0, "Saturation", now - 500);
        TestUtils.addRecord(storage, pid, 88.0, "Saturation", now);
        Patient p = storage.getPatient(pid);
        List<com.alerts.Alert> alerts = new ArrayList<>();
        new BloodSaturationAlerts().checkPatient(p, alerts);
        assertFalse(alerts.isEmpty(), "BloodSaturationAlerts should detect low saturation or rapid drop");
    }

    @Test
    public void hypotensiveHypoxemiaDetected() {
        DataStorage storage = new DataStorage();
        int pid = 4000;
        long now = System.currentTimeMillis();
        TestUtils.addRecord(storage, pid, 85.0, "Systolic", now);
        TestUtils.addRecord(storage, pid, 90.0, "BloodSaturation", now);
        Patient p = storage.getPatient(pid);
        List<com.alerts.Alert> alerts = new ArrayList<>();
        new HypotensiveHypoxemiaAlert().checkPatient(p, alerts);
        assertFalse(alerts.isEmpty(), "HypotensiveHypoxemiaAlert should detect condition when systolic<90 and saturation<92");
    }
}
