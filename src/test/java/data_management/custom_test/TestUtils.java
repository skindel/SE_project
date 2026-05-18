package data_management.custom_test;

import com.data_management.DataStorage;

public class TestUtils {
    public static void addRecord(DataStorage storage, int patientId, double value, String type, long ts) {
        storage.addPatientData(patientId, value, type, ts);
    }
}
