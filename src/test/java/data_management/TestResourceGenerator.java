package data_management;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Utility to generate resource-style test files used by the project.
 * Files follow the format: "Patient ID: <id>, Timestamp: <ts>, Label: <label>, Data: <value>"
 */
public class TestResourceGenerator {

    public static void writeAlertFile(Path dir, String fileName) throws IOException {
        File f = dir.resolve(fileName).toFile();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            // normal triggered entry
            w.write("Patient ID: 999, Timestamp: 1700000000000, Label: Alert, Data: Triggered\n");
            // normal non-triggered entry
            w.write("Patient ID: 1000, Timestamp: 1700000001000, Label: Alert, Data: Other\n");
            // edge case: extreme timestamp
            w.write("Patient ID: 1001, Timestamp: 99999999999999, Label: Alert, Data: Triggered\n");
            // malformed: missing Data
            w.write("Patient ID: 1002, Timestamp: 1700000002000, Label: Alert\n");
        }
    }

    public static void writeECGFile(Path dir, String fileName) throws IOException {
        File f = dir.resolve(fileName).toFile();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            // duplicate patient
            w.write("Patient ID: 200, Timestamp: 1700000000003, Label: ECG, Data: 0.34\n");
            
            for(int i = 0; i < 20; i++){
                w.write("Patient ID: 100, Timestamp: 1700000000003, Label: ECG, Data: 0.34\n");
            }
            //should set an alert
            w.write("Patient ID: 100, Timestamp: 1700000000003, Label: ECG, Data: 112.3\n");
        }
    }

    public static void writeSaturationFile(Path dir, String fileName) throws IOException {
        File f = dir.resolve(fileName).toFile();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            // percent sign should be stripped by parser
            w.write("Patient ID: 300, Timestamp: 1700000000100, Label: Saturation, Data: 98%\n");
            // zero value
            w.write("Patient ID: 301, Timestamp: 1700000000200, Label: Saturation, Data: 0%\n");
        }
    }

    public static void writeSystolicFile(Path dir, String fileName) throws IOException {
        File f = dir.resolve(fileName).toFile();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            w.write("Patient ID: 400, Timestamp: 1700000001000, Label: SystolicPressure, Data: 120\n");
            w.write("Patient ID: 400, Timestamp: 1700000002000, Label: SystolicPressure, Data: 135\n");
            w.write("Patient ID: 400, Timestamp: 1700000003000, Label: SystolicPressure, Data: 19000\n");
        }
    }

    public static void writeDiastolicFile(Path dir, String fileName) throws IOException {
        File f = dir.resolve(fileName).toFile();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            w.write("Patient ID: 400, Timestamp: 1700000001000, Label: DiastolicPressure, Data: -80\n");
            w.write("Patient ID: 400, Timestamp: 1700000002000, Label: DiastolicPressure, Data: 90\n");
            w.write("Patient ID: 400, Timestamp: 1700000003000, Label: DiastolicPressure, Data: 100000\n");
        }
    }

    public static void writeBloodCellsFiles(Path dir) throws IOException {
        File red = dir.resolve("RedBloodCells.txt").toFile();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(red))) {
            w.write("Patient ID: 500, Timestamp: 1700000004000, Label: RedBloodCells, Data: 4.5\n");
        }
        File white = dir.resolve("WhiteBloodCells.txt").toFile();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(white))) {
            w.write("Patient ID: 501, Timestamp: 1700000005000, Label: WhiteBloodCells, Data: 7.8\n");
        }
    }

    public static void writeCholesterolFile(Path dir) throws IOException {
        File f = dir.resolve("Cholesterol.txt").toFile();
        try (BufferedWriter w = new BufferedWriter(new FileWriter(f))) {
            w.write("Patient ID: 600, Timestamp: 1700000006000, Label: Cholesterol, Data: 5.2\n");
        }
    }

    public static void writeAllFiles(Path dir) throws IOException {
        writeAlertFile(dir, "Alert.txt");
        writeECGFile(dir, "ECG.txt");
        writeSaturationFile(dir, "Saturation.txt");
        writeSystolicFile(dir, "SystolicPressure.txt");
        writeDiastolicFile(dir, "DiastolicPressure.txt");
        writeBloodCellsFiles(dir);
        writeCholesterolFile(dir);
    }
}
