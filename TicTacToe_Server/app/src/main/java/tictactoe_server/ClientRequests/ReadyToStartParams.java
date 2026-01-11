package tictactoe_server.ClientRequests;

import com.fasterxml.jackson.annotation.JsonProperty;

import tictactoe_server.PlayerInfo;

public record ReadyToStartParams(
    @JsonProperty("playerInfo")  PlayerInfo playerInfo
){}