package tictactoe_server.service;

import java.util.ArrayList;
import java.util.List;

import tictactoe_server.logic.MatchLogic;
import tictactoe_server.model.Match;
import tictactoe_server.model.Player;

public enum MatchmakingService {
    INSTANCE;
    
    //This class is for creating matches with players

    private List<Match> pendingMatches = new ArrayList<Match>();
    private List<MatchLogic> activeMatches = new ArrayList<MatchLogic>();
    
    public void findMatchForUser(Player player) {
        if (pendingMatches.size() > 0) {
            
            var startingMatch = pendingMatches.get(0);
            pendingMatches.remove(0);

            startingMatch.addPlayer(player);
            var matchLogic = new MatchLogic();
            matchLogic.startMatch(startingMatch);
            activeMatches.add(matchLogic);
        } else {
            var newMatch = new Match();
            newMatch.addPlayer(player);
            pendingMatches.add(newMatch);
        }
    }
}
