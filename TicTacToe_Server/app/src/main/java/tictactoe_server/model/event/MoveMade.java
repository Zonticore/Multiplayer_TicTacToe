package tictactoe_server.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import tictactoe_server.model.Coordinate;
import tictactoe_server.model.PlayerInfo;

public record MoveMade(
        @JsonProperty("move") Coordinate move,
        @JsonProperty("playerInfo") PlayerInfo playerInfo
    ) {
}