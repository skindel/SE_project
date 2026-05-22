package com.testAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class DataStorageTests {

    @Test
    public void addAndRetrieve() {
        DataStorage storage = DataStorage.getInstance();
        long now = System.currentTimeMillis();

        storage.addPatientData(10, 120.0, "SystolicPressure", now - 2000);
        storage.addPatientData(10, 80.0, "DiastolicPressure", now - 1000);
        storage.addPatientData(10, 98.0, "Saturation", now);

        List<PatientRecord> records = storage.getRecords(10, now - 5000, now + 1000);
        assertNotNull(records);
        assertEquals(3, records.size());
    }

    @Test
    public void getAllPatientsAndGetPatient() {
        DataStorage storage = DataStorage.getInstance();
        storage.addPatientData(1, 100.0, "ECG", System.currentTimeMillis());
        storage.addPatientData(2, 95.0, "Saturation", System.currentTimeMillis());

        List<Patient> patients = storage.getAllPatients();
        assertEquals(2, patients.size());

        Patient p = storage.getPatient(1);
        assertNotNull(p);
    }
}
