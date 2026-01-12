package tictactoe_server.model;

import tictactoe_server.logic.MatchLogic;
import tictactoe_server.network.ClientContext;

public class Player {
    public PlayerInfo playerInfo;
    public ClientContext clientContext;
    public MatchLogic joinedMatch;
}
