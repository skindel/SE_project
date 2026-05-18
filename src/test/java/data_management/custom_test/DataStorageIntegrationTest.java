package data_management.custom_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

public class DataStorageIntegrationTest {

    @Test
    public void testAddAndRetrieveRecordsInRange() {
        DataStorage storage = new DataStorage();
        long now = System.currentTimeMillis();
        TestUtils.addRecord(storage, 1, 10.0, "ECG", now - 2000);
        TestUtils.addRecord(storage, 1, 20.0, "ECG", now - 1000);
        TestUtils.addRecord(storage, 1, 30.0, "ECG", now);

        List<PatientRecord> records = storage.getRecords(1, now - 1500, now + 100);
        assertEquals(2, records.size(), "Should return two records in the time range");
        assertTrue(records.get(0).getTimestamp() <= records.get(1).getTimestamp());
    }

    @Test
    public void testUpdateQueueEnqueued() {
        DataStorage storage = new DataStorage();
        long now = System.currentTimeMillis();
        TestUtils.addRecord(storage, 2, 1.0, "Alert", now);
        TestUtils.addRecord(storage, 2, 2.0, "ECG", now + 1);
        assertEquals(2, storage.getUpdateQueue().size(), "Two updates should be queued");
    }
}
