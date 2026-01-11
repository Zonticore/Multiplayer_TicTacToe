package tictactoe_server.ServerEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

import tictactoe_server.PlayerInfo;

public record OpponentsTurn(
        @JsonProperty("playerInfo") PlayerInfo playerInfo
    ) {
}