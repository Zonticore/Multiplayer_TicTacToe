package tictactoe_server;

import java.util.ArrayList;
import java.util.List;

import tictactoe_server.ServerEvents.GameOver;
import tictactoe_server.ServerEvents.MoveMade;
import tictactoe_server.ServerEvents.OpponentsTurn;
import tictactoe_server.ServerEvents.WinType;
import tictactoe_server.ServerEvents.YourTurn;

public class Match {
    
    public void addPlayer(PlayerInfo playerInfo, ClientContext clientContext) {
        var newPlayer = new Player();
        newPlayer.playerInfo = playerInfo;
        newPlayer.clientContext = clientContext;
        players.add(newPlayer);
    }

    public void startMatch() {
        for (int y = 0; y < moves.length; y++) {
            for (int x = 0; x < moves[y].length; x++) {
                moves[x][y] = ' ';
            }
        }
        System.out.println("startMatch!");
        informPlayersOfTurn();
    }

    public void playerMakesMove(int moveX, int moveY, ClientContext clientContext) throws Exception {
        Player player = null;
        for (int i = 0; i < players.size(); i++) {
            if (clientContext == players.get(i).clientContext) {
                player = players.get(i);
            }
        }
        if (moves[moveX][moveY] != ' ') {
            throw new Exception("Space already taken!");
        }
        moves[moveX][moveY] = player.playerInfo.playerChar();
        informOfMoveMade(moveX, moveY, player);

        WinType winType = checkHasPlayerWon(player);
        if (winType != null && winType != WinType.NONE) {
            sendGameOver(player.playerInfo, winType);
            return;
        } else if (!areAnyMovesLeft()) {
            sendGameOver(null, null);
            return;
        }

        nextPlayersTurn();
    }
    
    private boolean areAnyMovesLeft() {
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (moves[x][y] == ' ') {
                    return true;
                }
            }
        }
        return false;
    }


    private WinType checkHasPlayerWon(Player player) {
        char playerChar = player.playerInfo.playerChar();

        for (int row = 0; row < BOARD_SIZE; row++) {
            List<Coordinate> positions = checkRow(playerChar, row);
            if (positions != null) {
                return WinType.ROW;
            }
        }

        for (int col = 0; col < BOARD_SIZE; col++) {
            List<Coordinate> positions = checkColumn(playerChar, col);
            if (positions != null) {
                return WinType.COLUMN;
            }
        }

        List<Coordinate> positions = checkDiagonal(playerChar, true);
        if (positions != null) {
            return WinType.MAIN_DIAGONAL;
        }

        positions = checkDiagonal(playerChar, false);
        if (positions != null) {
            return WinType.ANTI_DIAGONAL;
        }

        return WinType.NONE;
    }

    private List<Coordinate> checkDiagonal(char playerChar, boolean isMainDiagonal) {
        List<Coordinate> positions = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            int col = isMainDiagonal ? i : BOARD_SIZE - 1 - i;
            if (moves[i][col] != playerChar) {
                return null;
            }
            positions.add(new Coordinate(i, col));
        }
        return positions;
    }

    private List<Coordinate> checkColumn(char playerChar, int col) {
        List<Coordinate> positions = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (moves[row][col] != playerChar) {
                return null;
            }
            positions.add(new Coordinate(row, col));
        }
        return positions;
    }

    private List<Coordinate> checkRow(char playerChar, int row) {
        List<Coordinate> positions = new ArrayList<>();
        for (int col = 0; col < BOARD_SIZE; col++) {
            if (moves[row][col] != playerChar) {
                return null;
            }
            positions.add(new Coordinate(row, col));
        }
        return positions;
    }

    public void nextPlayersTurn() {
        currentTurnPlayerIndex++;
        if (currentTurnPlayerIndex >= players.size()) currentTurnPlayerIndex = 0;
        informPlayersOfTurn();
    }

    private void informPlayersOfTurn() {
        for (int i = 0; i < players.size(); i++) {
            if (i == currentTurnPlayerIndex) {
                var yourTurnData = new YourTurn();
                System.out.println("Sending your turn to player at index " + i);
                players.get(i).clientContext.sendEvent(EventRegistry.yourTurn, yourTurnData);
            } else {
                var opponentsTurnData = new OpponentsTurn(players.get(i).playerInfo);
                System.out.println("Sending Opponents turn to player at index " + i);
                players.get(i).clientContext.sendEvent(EventRegistry.opponentsTurn, opponentsTurnData);
            }
        }
    }

    private void informOfMoveMade(int moveX, int moveY, Player player) {
        for (int i = 0; i < players.size(); i++) {
            var opponentsTurnData = new MoveMade(moveX, moveY, player.playerInfo);
            players.get(i).clientContext.sendEvent(EventRegistry.moveMade, opponentsTurnData);
        }
    }

    private void sendGameOver(PlayerInfo playerInfo, WinType winType) {
        for (int i = 0; i < players.size(); i++) {
            var opponentsTurnData = new GameOver(playerInfo, winType);
            players.get(i).clientContext.sendEvent(EventRegistry.gameOver, opponentsTurnData);
        }
    }
    
    private int currentTurnPlayerIndex;
    private char[][] moves = new char[BOARD_SIZE][BOARD_SIZE];
    private List<Player> players = new ArrayList<>();

    private final static int BOARD_SIZE = 3;
}
