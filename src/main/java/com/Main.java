package com;

import com.data_management.DataStorage;
import com.data_management.WebSocketClient;

import java.net.URI;
import java.util.Random;

import com.alerts.AlertManager;
import com.cardio_generator.HealthDataSimulator;

public class Main {
    private static final Random random = new Random();

    public static void main(String[] args) {

         // start the simulator (generators may produce file output or other transports)
        try {
            HealthDataSimulator.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int port = 8080;

        String wsUri = "ws://localhost:"+port;

        // create storage and alert manager
        DataStorage dataStorage = DataStorage.getInstance();
        AlertManager alertManager = new AlertManager(dataStorage);

        WebSocketClient wsClient = null;
        try {
            wsClient = new WebSocketClient(new URI(wsUri), dataStorage);
            wsClient.startClient();
        } catch (Exception e) {
            System.err.println("Failed to start WebSocket client: " + e.getMessage());
            e.printStackTrace();
        }

        // graceful shutdown: stop websocket client on JVM exit
        final WebSocketClient finalClient = wsClient;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (finalClient != null) {
                finalClient.stopClient();
            }
        }));

        // keep main thread alive while background threads run
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
