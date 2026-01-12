package tictactoe_server.model;

import java.util.ArrayList;
import java.util.List;

import tictactoe_server.logic.BoardLogic;

public class Match {
    
    public void addPlayer(Player player) {
        players.add(player);
    }
    
    public BoardLogic boardLogic;
    public int currentTurnPlayerIndex;
    public List<Player> players = new ArrayList<>();
}
