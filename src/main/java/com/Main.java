package com;

import com.data_management.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.alerts.*;
import com.cardio_generator.generators.*;
import com.cardio_generator.outputs.*;
import com.cardio_generator.HealthDataSimulator;
import com.cardio_generator.generators.AlertGenerator;
import com.cardio_generator.generators.BloodLevelsDataGenerator;
import com.cardio_generator.generators.BloodPressureDataGenerator;
import com.cardio_generator.generators.BloodSaturationDataGenerator;
import com.cardio_generator.generators.ECGDataGenerator;


public class Main {
    private static final Random random = new Random();
    private static ScheduledExecutorService scheduler;
    public static void main(String[] args) {
        String baseDirectory = "src/main/resources";
        
        DataStorage dataStorage = DataStorage.getInstance();
        FileDataReader dataReader = new FileDataReader(baseDirectory, dataStorage);
        AlertManager alertManager = new AlertManager(dataStorage);

        try{
            HealthDataSimulator.main(args);
            dataReader.readData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }

}
