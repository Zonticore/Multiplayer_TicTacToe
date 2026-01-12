package tictactoe_server.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlayerInfo(
        @JsonProperty("playerName") String playerName,
        @JsonProperty("playerChar") char playerChar) {
}