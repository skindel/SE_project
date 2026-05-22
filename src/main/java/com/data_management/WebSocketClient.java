package com.data_management;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket client that connects to a server, parses incoming messages and stores
 *
 * The parser will special-case `Alert` and `Saturation` similarly to
 * FileDataReader.
 */
public class WebSocketClient extends org.java_websocket.client.WebSocketClient {
    private final DataStorage dataStorage;

    public WebSocketClient(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
    }

    @Override
    public void onOpen(org.java_websocket.handshake.ServerHandshake handshakedata) {
        System.out.println("WebSocket connected: " + getURI());
    }

    @Override
    public void onMessage(String message) {
        // System.out.println(message);
        if (message == null || message.isEmpty()) return;
        // messages may contain multiple lines; process each
        String[] lines = message.split("\\r?\\n");
        for (String line : lines) {
            processData(line);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket closed: " + getURI() + " code=" + code + " reason=" + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
        ex.printStackTrace();
    }

    /**
     * Parses a single console-style data line and stores it in DataStorage.
     */
    private void processData(String line) {
        // System.out.println("Captured "+line);
        try {
            String[] parts = line.split(",");

            String label = parts[2];
            String data = parts[3];

            if ("Alert".equals(label)) {
                if ("triggered".equalsIgnoreCase(data)) {
                    data = "1";
                } else {
                    data = "0";
                }
            }

            if ("Saturation".equals(label)) {
                data = data.replace("%", "");
            }

            int patientId = Integer.parseInt(parts[0]);
            double measurementValue = Double.parseDouble(data);
            long timestamp = Long.parseLong(parts[1]);

            dataStorage.addPatientData(patientId, measurementValue, label, timestamp);
        } catch (Exception ex) {
            System.err.println("Failed to process websocket message line: '" + line + "' - " + ex.getMessage());
        }
    }

    /** Convenience start method. */
    public void startClient() {
        this.connect();
    }

    /** Convenience stop method. */
    public void stopClient() {
        try {
            this.closeBlocking();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
