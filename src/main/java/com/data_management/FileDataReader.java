package com.data_management;

import java.io.*;

import java.util.*;
import java.util.concurrent.*;

import com.alerts.AlertManager;

/**
 * Reads and saves data from files specified in baseDirectory (filenames hardcoded due to the)
 * nature of the generators. 
 */
public class FileDataReader implements DataReader {
    private String baseDirectory;
    private static ScheduledExecutorService scheduler;
    private DataStorage dataStorage;
    // keeps track of last line read in each file to avoid re-reading the entire file eat each call.
    private Map<String, Long> lastFilePositions = new ConcurrentHashMap<>();

    /**
     * @param baseDirectory directory where all data files are saved and edited.
     */
    public FileDataReader(String baseDirectory, DataStorage dataStorage) {
        this.baseDirectory = baseDirectory;;
        this.dataStorage = dataStorage;
        // one thread per each file
        scheduler = Executors.newScheduledThreadPool(8);
    }
    
    /**
     * Scheldules readings of all specified files in same intervals as the the generators in {@link com.cardio_generator.HealthDataSimulator}
     */
    public void readData() throws IOException{
        scheduleTask(() -> startTailing("ECG.txt"), 1, TimeUnit.SECONDS);
        scheduleTask(() -> startTailing("Saturation.txt"), 1, TimeUnit.SECONDS);
        scheduleTask(() -> startTailing("DiastolicPressure.txt"), 1, TimeUnit.MINUTES);
        scheduleTask(() -> startTailing("SystolicPressure.txt"), 1, TimeUnit.MINUTES);
        scheduleTask(() -> startTailing("RedBloodCells.txt"), 2, TimeUnit.MINUTES);
        scheduleTask(() -> startTailing("WhiteBloodCells.txt"), 2, TimeUnit.MINUTES);
        scheduleTask(() -> startTailing("Cholesterol.txt"), 2, TimeUnit.MINUTES);
        scheduleTask(() -> startTailing("Alert.txt"), 5, TimeUnit.SECONDS);
    }

    /**
     * Reads a file from baseDirectory/filename fom the last read line, keeps track of the last line.
     * Calls processData for each line.
     * @param fileName name of the file (in baseDirectory)
     */
    private void startTailing(String fileName) {
        File file = new File(baseDirectory, fileName);
        long lastKnownPosition = lastFilePositions.getOrDefault(fileName, 0L);

        if (file.exists() && file.length() > lastKnownPosition) {
            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                
                // find last pos
                raf.seek(lastKnownPosition);
                
                String line;
                while ((line = raf.readLine()) != null) {
                    processData(line);
                }
                
                // Update last pos
                lastKnownPosition = raf.getFilePointer();
                lastFilePositions.put(fileName, lastKnownPosition);
                
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        }

    }
    
    /**
     * Processes a line (data entry), converts to correct datatypes while specialcasing some log types.
     * Saves the entries in dataStorage in correct format.
     * 
     * @param line one entry from any of the data files
     */
    public void processData(String line){
        String[] parts = line.split(", ");

        Map<String, String> vals = new HashMap<>();
        for(String p : parts){
            String[] keyVals = p.split(": ");
            vals.put(keyVals[0], keyVals[1]);
        }
    
        if(vals.get("Label").equals("Alert")){
            // numerical encoding of triggered/other for easier processing alerts
            if(vals.get("Data").equals("triggered")){
                vals.put("Data", "1");
            } else {
                vals.put("Data", "0");
            }
        }

        if(vals.get("Label").equals("Saturation")){
            //remove % sign
            vals.put("Data", vals.get("Data").replace("%", ""));
        }

        int patientId = Integer.parseInt(vals.get("Patient ID"));
        double measurementValue = Double.parseDouble(vals.get("Data"));
        String label = vals.get("Label");
        long timestamp = Long.parseLong(vals.get("Timestamp"));
        // System.out.println("Read data for patient " + patientId + " at " + timestamp + " for " + label + ": " + measurementValue);
        dataStorage.addPatientData(patientId, measurementValue, label, timestamp);
    }

    /**
     * Schedules a task to run periodically at fixed intervals.
     * 
     * @param task the runnable task to schedule
     * @param period the period between task executions
     * @param timeUnit the time unit for the period
     */
    private static void scheduleTask(Runnable task, long period, TimeUnit timeUnit) {
        scheduler.scheduleAtFixedRate(task, 5, period, timeUnit);
    }
}