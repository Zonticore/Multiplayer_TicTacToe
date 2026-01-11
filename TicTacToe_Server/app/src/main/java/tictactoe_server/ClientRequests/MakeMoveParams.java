package tictactoe_server.ClientRequests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MakeMoveParams(
        @JsonProperty("moveX") int moveX,
        @JsonProperty("moveY") int moveY) {
}