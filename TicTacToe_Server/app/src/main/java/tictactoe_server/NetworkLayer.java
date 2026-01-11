package tictactoe_server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tictactoe_server.ClientRequests.ClientRequest;
import tictactoe_server.ClientRequests.MakeMove;
import tictactoe_server.ClientRequests.ReadyToStartRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class NetworkLayer {

    private static final int PORT = 5555;
    private static final ObjectMapper mapper = new ObjectMapper();

    public void start() {
        System.out.println("TCP Server starting on port: " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread.ofVirtual().name("client-handler").start(() -> {
                    handleClient(clientSocket);
                });
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private void handleClient(Socket socket) {
        String clientAddr = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        System.out.println("New client connected: " + clientAddr);

        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)) {

            ClientContext clientContext = new ClientContext(out, clientAddr, mapper);
            String line;

            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                try {
                    JsonNode json = mapper.readTree(line);
                    System.out.println("[" + clientAddr + "] Received JSON: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));

                    processMessage(json, clientContext);

                } catch (Exception e) {
                    System.out.println("[" + clientAddr + "] Invalid JSON: " + line);

                    ObjectNode error = mapper.createObjectNode();
                    error.put("MessageType", "Response");
                    error.put("type", "error");
                    error.put("message", "Invalid JSON format");
                    error.put("details", e.getMessage());
                    error.put("received", line);

                    clientContext.send(error);
                }
            }
        } catch (IOException e) {
            System.out.println("Client " + clientAddr + " error: " + e.getMessage());
        } finally {
            System.out.println("Client disconnected: " + clientAddr);
        }
    }

    private void processMessage(JsonNode message, ClientContext context) {
        try {
            String msgType = message.path("MessageType").asText("").toLowerCase();

            if (msgType.isEmpty()) {
                context.sendError("Missing 'MessageType' field");
                return;
            }

            if (msgType.equals("request")) {
                processRequest(message, context);
            } else if (msgType.equals("event")) {
                processEvent(message, context);
            } else {
                context.sendError("Unknown MessageType: " + msgType);
            }

        } catch (Exception e) {
            context.sendError("Invalid message format: " + e.getMessage());
        }
    }

    private void processRequest(JsonNode message, ClientContext context) {
        try {
            String type = message.path("Type").asText("").toLowerCase();

            if (type.isEmpty()) {
                context.sendError("Missing 'Type' field in request");
                return;
            }

            JsonNode params = message.path("Param");
            if (params.isMissingNode()) {
                params = mapper.createObjectNode();
            }

            String requestId = message.path("RequestId").asText(null);

            ClientRequest request = createRequest(type, params);

            if (request == null) {
                context.sendError("Unknown request type: " + type, requestId);
                return;
            }

            var response = request.run(context);
            context.sendResponse(response, requestId);

        } catch (Exception e) {
            context.sendError("Invalid request format: " + e.getMessage());
        }
    }

    private void processEvent(JsonNode message, ClientContext context) {
        String eventType = message.path("EventType").asText("").toLowerCase();

        if (eventType.isEmpty()) {
            context.sendError("Missing 'EventType' field in event");
            return;
        }

        JsonNode data = message.path("Data");
        if (data.isMissingNode()) {
            data = mapper.createObjectNode();
        }

        // Handle incoming events from client here
        // For now, log it; extend as needed for your game logic
        context.log("Received event: " + eventType + " with data: " + data.toPrettyString());

        // Example: If client sends "Ready", trigger game start, etc.
        // You can add switch(eventType) { ... }
    }

    private ClientRequest createRequest(String type, JsonNode params) {
        return switch (type) {
            case "make-move" ->      new MakeMove(params);
            case "ready-to-start" -> new ReadyToStartRequest(params);
            default -> null;
        };
    }
}