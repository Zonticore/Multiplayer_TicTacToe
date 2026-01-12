package tictactoe_server.logic;

import tictactoe_server.model.Coordinate;
import tictactoe_server.model.Match;
import tictactoe_server.model.Player;
import tictactoe_server.model.PlayerInfo;
import tictactoe_server.model.WinType;
import tictactoe_server.model.event.GameOver;
import tictactoe_server.model.event.MoveMade;
import tictactoe_server.model.event.OpponentsTurn;
import tictactoe_server.model.event.YourTurn;
import tictactoe_server.network.ClientContext;
import tictactoe_server.service.EventRegistry;

public class MatchLogic {
    
    public void startMatch(Match runningMatch) {
        this.runningMatch = runningMatch;
        for (int i = 0; i < runningMatch.players.size(); i++) {
            runningMatch.players.get(i).joinedMatch = this;
        }
        runningMatch.boardLogic = new BoardLogic();
        System.out.println("startMatch!");
        informPlayersOfTurn();
    }

    public void playerMakesMove(Coordinate move, ClientContext clientContext) throws Exception {
        Player player = null;
        for (int i = 0; i < runningMatch.players.size(); i++) {
            if (clientContext == runningMatch.players.get(i).clientContext) {
                player = runningMatch.players.get(i);
            }
        }
        runningMatch.boardLogic.makeMove(move, player.playerInfo.playerChar());
        informOfMoveMade(move, player);

        WinType winType = runningMatch.boardLogic.checkHasPlayerWon(player);
        if (winType != null && winType != WinType.NONE) {
            sendGameOver(player.playerInfo, winType);
            return;
        } else if (!runningMatch.boardLogic.areAnyMovesLeft()) {
            sendGameOver(null, null);
            return;
        }

        nextPlayersTurn();
    }

    public void nextPlayersTurn() {
        runningMatch.currentTurnPlayerIndex++;
        if (runningMatch.currentTurnPlayerIndex >= runningMatch.players.size()) runningMatch.currentTurnPlayerIndex = 0;
        informPlayersOfTurn();
    }

    private void informPlayersOfTurn() {
        for (int i = 0; i < runningMatch.players.size(); i++) {
            if (i == runningMatch.currentTurnPlayerIndex) {
                var yourTurnData = new YourTurn();
                System.out.println("Sending your turn to player at index " + i);
                runningMatch.players.get(i).clientContext.sendEvent(EventRegistry.yourTurn, yourTurnData);
            } else {
                var opponentsTurnData = new OpponentsTurn(runningMatch.players.get(runningMatch.currentTurnPlayerIndex).playerInfo);
                System.out.println("Sending Opponents turn to player at index " + i);
                runningMatch.players.get(i).clientContext.sendEvent(EventRegistry.opponentsTurn, opponentsTurnData);
            }
        }
    }

    private void informOfMoveMade(Coordinate move, Player player) {
        for (int i = 0; i < runningMatch.players.size(); i++) {
            var opponentsTurnData = new MoveMade(move, player.playerInfo);
            runningMatch.players.get(i).clientContext.sendEvent(EventRegistry.moveMade, opponentsTurnData);
        }
    }

    private void sendGameOver(PlayerInfo playerInfo, WinType winType) {
        for (int i = 0; i < runningMatch.players.size(); i++) {
            var opponentsTurnData = new GameOver(playerInfo, winType);
            runningMatch.players.get(i).clientContext.sendEvent(EventRegistry.gameOver, opponentsTurnData);
        }
    }

    private Match runningMatch;
}
