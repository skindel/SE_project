package com.data_management;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket client that connects to a server, parses incoming messages and stores
 * them into the provided DataStorage. Messages are expected in the same text
 * format produced by FileOutputStrategy / AlertManager, e.g.
 *
 * Patient ID: 1, Timestamp: 1234567890, Label: Saturation, Data: 98%
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
        try {
            String[] parts = line.split(", ");
            Map<String, String> vals = new HashMap<>();
            for (String p : parts) {
                String[] keyVals = p.split(": ", 2);
                if (keyVals.length == 2) {
                    vals.put(keyVals[0], keyVals[1]);
                }
            }

            if (!vals.containsKey("Patient ID") || !vals.containsKey("Timestamp") || !vals.containsKey("Label") || !vals.containsKey("Data")) {
                // not a recognized message format; ignore
                return;
            }

            String label = vals.get("Label");
            String data = vals.get("Data");

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

            int patientId = Integer.parseInt(vals.get("Patient ID"));
            double measurementValue = Double.parseDouble(data);
            long timestamp = Long.parseLong(vals.get("Timestamp"));

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
