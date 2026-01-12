package tictactoe_server.logic;

import java.util.ArrayList;
import java.util.List;

import tictactoe_server.model.Coordinate;
import tictactoe_server.model.Player;
import tictactoe_server.model.WinType;

public class BoardLogic {

    public BoardLogic() {
        initBoard();
    }

    private void initBoard() {
        for (int y = 0; y < moves.length; y++) {
            for (int x = 0; x < moves[y].length; x++) {
                moves[x][y] = ' ';
            }
        }
    }

    public void makeMove(Coordinate move, char moveChar) throws Exception {
        if (moves[move.x().pos()][move.y().pos()] != ' ') {
            throw new Exception("Space already taken!");
        }
        moves[move.x().pos()][move.y().pos()] = moveChar;
    }

    public boolean areAnyMovesLeft() {
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (moves[x][y] == ' ') {
                    return true;
                }
            }
        }
        return false;
    }


    public WinType checkHasPlayerWon(Player player) {
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
            
            positions.add(Coordinate.makeCoordinate(i, col, BOARD_SIZE));
        }
        return positions;
    }

    private List<Coordinate> checkColumn(char playerChar, int col) {
        List<Coordinate> positions = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (moves[row][col] != playerChar) {
                return null;
            }
            positions.add(Coordinate.makeCoordinate(row, col, BOARD_SIZE));
        }
        return positions;
    }

    private List<Coordinate> checkRow(char playerChar, int row) {
        List<Coordinate> positions = new ArrayList<>();
        for (int col = 0; col < BOARD_SIZE; col++) {
            if (moves[row][col] != playerChar) {
                return null;
            }
            positions.add(Coordinate.makeCoordinate(row, col, BOARD_SIZE));
        }
        return positions;
    }

    private char[][] moves = new char[BoardLogic.BOARD_SIZE][BoardLogic.BOARD_SIZE];

    public final static int BOARD_SIZE = 3;
}
