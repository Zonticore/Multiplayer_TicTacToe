package tictactoe_server.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import tictactoe_server.model.PlayerInfo;

public record OpponentsTurn(
        @JsonProperty("playerInfo") PlayerInfo playerInfo
    ) {
}