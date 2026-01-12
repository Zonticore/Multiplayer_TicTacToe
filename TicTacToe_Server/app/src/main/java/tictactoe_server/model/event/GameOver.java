package tictactoe_server.model.event;

import tictactoe_server.model.PlayerInfo;
import tictactoe_server.model.WinType;

public record GameOver(
        PlayerInfo winner,
        WinType winType) {
}