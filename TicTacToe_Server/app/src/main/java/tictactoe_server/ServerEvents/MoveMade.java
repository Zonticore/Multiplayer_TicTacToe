package tictactoe_server.ServerEvents;

import com.fasterxml.jackson.annotation.JsonProperty;

import tictactoe_server.PlayerInfo;

public record MoveMade(
        @JsonProperty("moveX") int moveX,
        @JsonProperty("moveY") int moveY,
        @JsonProperty("playerInfo") PlayerInfo playerInfo
    ) {
}