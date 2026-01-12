package tictactoe_server.model;

import tictactoe_server.logic.BoardLogic;

public class GameBoard {
    public char[][] moves = new char[BoardLogic.BOARD_SIZE][BoardLogic.BOARD_SIZE];
    public final static int BOARD_SIZE = 3;
}
