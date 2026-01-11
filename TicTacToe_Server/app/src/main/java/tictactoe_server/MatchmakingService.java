package tictactoe_server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum MatchmakingService {
    INSTANCE;
    
    private List<Match> pendingMatches = new ArrayList<Match>();
    private List<Match> activeMatches = new ArrayList<Match>();
    
    public void findMatchForUser(PlayerInfo playerInfo, ClientContext clientContext) {
        if (pendingMatches.size() > 0) {
            var startingMatch = pendingMatches.get(0);
            activeMatches.add(startingMatch);
            pendingMatches.remove(0);
            playersInMatches.put(clientContext, startingMatch);
            startingMatch.addPlayer(playerInfo, clientContext);
            startingMatch.startMatch();
        } else {
            var newMatch = new Match();
            newMatch.addPlayer(playerInfo, clientContext);
            playersInMatches.put(clientContext, newMatch);
            pendingMatches.add(newMatch);
        }
    }

    public Match getMatchForPlayer(ClientContext player) {
        return playersInMatches.get(player);
    }
    
    private HashMap<ClientContext, Match> playersInMatches = new HashMap<>();
}
