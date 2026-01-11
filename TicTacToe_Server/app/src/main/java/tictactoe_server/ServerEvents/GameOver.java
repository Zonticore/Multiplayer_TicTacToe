package tictactoe_server.ServerEvents;

import tictactoe_server.PlayerInfo;

public record GameOver(
        PlayerInfo winner,
        WinType winType) {
}