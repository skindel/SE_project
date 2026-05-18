package data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.alerts.AlertManager;

/**
 * Integration test that feeds generated resource files into the parser
 * and waits so AlertManager's background scheduler can print alerts.
 *
 * This test intentionally does not capture stdout so you can see
 * alert output in the Maven console when running `mvn test`.
 */
public class ShowAlertsIntegrationTest {

    @Test
    public void showAlertsPrintedToConsole() throws Exception {
        Path dir = Path.of("src", "test", "resources", "generated");
        TestResourceGenerator.writeAllFiles(dir);

        DataStorage storage = new DataStorage();
        FileDataReader reader = new FileDataReader(dir.toString(), storage);

        Method processData = FileDataReader.class.getDeclaredMethod("processData", String.class);
        processData.setAccessible(true);

        Files.list(dir).forEach(path -> {
            try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
                String line;

                while ((line = br.readLine()) != null) {
                    try {
                        processData.invoke(reader, line);
                    } catch (Exception e) {
                        // ignore parsing issues for this integration run
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // wait for AlertManager to pick up queued updates and print
        Thread.sleep(5000);
    }
}
