package tictactoe_server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tictactoe_server.ClientRequests.Response;

import java.io.PrintWriter;

public record ClientContext(
    PrintWriter out,
    String clientAddress,
    ObjectMapper mapper
) {
    public void send(Object response) {
        try {
            String json = mapper.writeValueAsString(response);
            out.println(json);
            out.flush();
        } catch (Exception e) {
            out.println("{\"MessageType\":\"Response\",\"type\":\"error\",\"message\":\"Server serialization error\"}");
            out.flush();
            log("Serialization error: " + e.getMessage());
        }
    }

    public void sendResponse(Response response, String requestId) {
        ObjectNode resp = mapper.createObjectNode();
        resp.put("MessageType", "Response");
        if (requestId != null) {
            resp.put("RequestId", requestId);
        }
        resp.put("Success", response.success());

        send(resp);
    }

    public void sendEvent(String eventType, Object data) {
        ObjectNode event = mapper.createObjectNode();
        event.put("MessageType", "Event");
        event.put("EventType", eventType);

        if (data != null) {
            event.set("Data", mapper.valueToTree(data));
        }

        send(event);
    }

    public void sendError(String message) {
        sendError(message, null);
    }

    public void sendError(String message, String requestId) {
        ObjectNode err = mapper.createObjectNode();
        err.put("MessageType", "Response");
        if (requestId != null) {
            err.put("RequestId", requestId);
        }
        err.put("type", "error");
        err.put("Success", false);
        err.put("message", message != null ? message : "Unknown error");

        send(err);
    }

    public void log(String message) {
        System.out.println("[" + clientAddress + "] " + message);
    }
}