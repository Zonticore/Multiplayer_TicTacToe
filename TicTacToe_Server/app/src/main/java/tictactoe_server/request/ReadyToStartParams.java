package tictactoe_server.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import tictactoe_server.model.PlayerInfo;

public record ReadyToStartParams(
    @JsonProperty("playerInfo")  PlayerInfo playerInfo
){}