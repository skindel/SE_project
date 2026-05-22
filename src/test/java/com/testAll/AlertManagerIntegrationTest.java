package com.testAll;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.alerts.AlertManager;
import com.data_management.DataStorage;
import com.alertTest.OutputReader;

public class AlertManagerIntegrationTest {

    @Test
    public void integrationProducesConsoleAlerts() throws Exception {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try {
            DataStorage storage = new DataStorage();
            AlertManager manager = new AlertManager(storage);

            long now = System.currentTimeMillis();
            // trigger a simple alert: Alert type triggered
            storage.addPatientData(42, 1.0, "Alert", now);

            long start = System.currentTimeMillis();
            while(!storage.getUpdateQueue().isEmpty() && System.currentTimeMillis() - start < 2000) Thread.sleep(50);
            Thread.sleep(200);

            String out = baos.toString("UTF-8");
            List<com.alertTest.OutputLine> parsed = OutputReader.parse(out);
            assertTrue(parsed.stream().anyMatch(l -> l.getPatientId() == 42));

            // shutdown scheduler (best-effort)
            try {
                java.lang.reflect.Field f = AlertManager.class.getDeclaredField("scheduler");
                f.setAccessible(true);
                Object sched = f.get(manager);
                if (sched instanceof java.util.concurrent.ExecutorService) {
                    ((java.util.concurrent.ExecutorService) sched).shutdownNow();
                }
            } catch (NoSuchFieldException nsf) {
                // ignore
            }
        } finally {
            System.setOut(originalOut);
        }
    }
}
