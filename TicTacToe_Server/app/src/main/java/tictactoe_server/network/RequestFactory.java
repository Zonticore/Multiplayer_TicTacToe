package tictactoe_server.network;

import com.fasterxml.jackson.databind.JsonNode;

import tictactoe_server.request.ClientRequest;
import tictactoe_server.request.MakeMove;
import tictactoe_server.request.ReadyToStartRequest;

public class RequestFactory {
    public static ClientRequest createRequest(String type, JsonNode params) {
        return switch (type) {
            case "make-move" ->      new MakeMove(params);
            case "ready-to-start" -> new ReadyToStartRequest(params);
            default -> null;
        };
    }
}
