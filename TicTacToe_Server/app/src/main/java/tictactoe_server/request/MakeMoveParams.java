package tictactoe_server.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import tictactoe_server.model.Coordinate;

public record MakeMoveParams(
        @JsonProperty("move") Coordinate move) {
}