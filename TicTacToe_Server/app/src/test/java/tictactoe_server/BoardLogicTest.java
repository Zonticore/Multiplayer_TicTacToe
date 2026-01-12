package tictactoe_server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import tictactoe_server.logic.BoardLogic;
import tictactoe_server.model.Coordinate;
import tictactoe_server.model.Player;
import tictactoe_server.model.PlayerInfo;
import tictactoe_server.model.WinType;

class BoardLogicTest {

    private BoardLogic board;
    private Player playerX;
    private Player playerO;

    @BeforeEach
    void setUp() {
        board = new BoardLogic();
        playerX = new Player();
        playerX.playerInfo = new PlayerInfo("testA", 'X');

        playerO = new Player();
        playerO.playerInfo = new PlayerInfo("testB", 'O');
    }

    private Coordinate makeCoordinate(int x, int y) {
        return Coordinate.makeCoordinate(x, y, 3);
    }

    @Test
    void testInitialization() {
        assertTrue(board.areAnyMovesLeft(), "Board should have moves left initially");
        assertEquals(WinType.NONE, board.checkHasPlayerWon(playerX), "No win initially");
    }

    @Test
    void testMakeValidMove() throws Exception {
        board.makeMove(makeCoordinate(0, 0), 'X');
        // Add assertion to check internal state if getter is added, or indirectly via win checks.
        assertEquals(WinType.NONE, board.checkHasPlayerWon(playerX), "Single move shouldn't win");
    }

    @Test
    void testMakeInvalidMove() {
        assertThrows(Exception.class, () -> {
            board.makeMove(makeCoordinate(0, 0), 'X');
            board.makeMove(makeCoordinate(0, 0), 'O');
        }, "Should throw exception for occupied space");
    }

    @Test
    void testRowWin() throws Exception {
        // RowWin
        //    a     b     c
        //       |     |
        // 1  X  |  X  |  X
        //  _____|_____|_____
        //       |     |
        // 2     |     |  
        //  _____|_____|_____
        //       |     |
        // 3     |     |   
        //       |     |
        board.makeMove(makeCoordinate(0, 0), 'X');
        board.makeMove(makeCoordinate(0, 1), 'X');
        board.makeMove(makeCoordinate(0, 2), 'X');
        assertEquals(WinType.ROW, board.checkHasPlayerWon(playerX), "Should detect row win");
    }

    @Test
    void testColumnWin() throws Exception {
        // ColumnWin
        //    a     b     c
        //       |     |
        // 1  O  |     |  
        //  _____|_____|_____
        //       |     |
        // 2  O  |     |  
        //  _____|_____|_____
        //       |     |
        // 3  O  |     |  
        //       |     |
        board.makeMove(makeCoordinate(0, 0), 'O');
        board.makeMove(makeCoordinate(1, 0), 'O');
        board.makeMove(makeCoordinate(2, 0), 'O');
        assertEquals(WinType.COLUMN, board.checkHasPlayerWon(playerO), "Should detect column win");
    }

    @Test
    void testMainDiagonalWin() throws Exception {
        // MainDiagonalWin
        //    a     b     c
        //       |     |
        // 1  X  |     |  
        //  _____|_____|_____
        //       |     |
        // 2     |  X  |  
        //  _____|_____|_____
        //       |     |
        // 3     |     |  X
        //       |     |
        board.makeMove(makeCoordinate(0, 0), 'X');
        board.makeMove(makeCoordinate(1, 1), 'X');
        board.makeMove(makeCoordinate(2, 2), 'X');
        assertEquals(WinType.MAIN_DIAGONAL, board.checkHasPlayerWon(playerX), "Should detect main diagonal win");
    }

    @Test
    void testAntiDiagonalWin() throws Exception {
        // AntiDiagonalWin
        //    a     b     c
        //       |     |
        // 1     |     |  O
        //  _____|_____|_____
        //       |     |
        // 2     |  O  |  
        //  _____|_____|_____
        //       |     |
        // 3  O  |     |  
        //       |     |
        board.makeMove(makeCoordinate(0, 2), 'O');
        board.makeMove(makeCoordinate(1, 1), 'O');
        board.makeMove(makeCoordinate(2, 0), 'O');
        assertEquals(WinType.ANTI_DIAGONAL, board.checkHasPlayerWon(playerO), "Should detect anti-diagonal win");
    }

    @Test
    void testNoWin() throws Exception {
        // Moves left but without win
        //    a     b     c
        //       |     |
        // 1  X  |  O  |   
        //  _____|_____|_____
        //       |     |
        // 2  O  |  X  |  
        //  _____|_____|_____
        //       |     |
        // 3  X  |  O  |  
        //       |     |
        board.makeMove(makeCoordinate(0, 0), 'X');
        board.makeMove(makeCoordinate(0, 1), 'O');
        board.makeMove(makeCoordinate(0, 2), 'X');
        board.makeMove(makeCoordinate(1, 0), 'O');
        board.makeMove(makeCoordinate(1, 1), 'X');
        board.makeMove(makeCoordinate(1, 2), 'O');
        assertEquals(WinType.NONE, board.checkHasPlayerWon(playerX));
        assertEquals(WinType.NONE, board.checkHasPlayerWon(playerO));
    }

    @Test
    void testDrawNoMovesLeft() throws Exception {

        // Draw position
        //    a     b     c
        //       |     |
        // 1  X  |  X  |  O
        //  _____|_____|_____
        //       |     |
        // 2  O  |  O  |  X
        //  _____|_____|_____
        //       |     |
        // 3  X  |  O  |  X
        //       |     |
        board.makeMove(makeCoordinate(0, 0), 'X');
        board.makeMove(makeCoordinate(0, 2), 'X');
        board.makeMove(makeCoordinate(1, 0), 'X');
        board.makeMove(makeCoordinate(2, 1), 'X');
        board.makeMove(makeCoordinate(2, 2), 'X');
        board.makeMove(makeCoordinate(1, 2), 'O');
        board.makeMove(makeCoordinate(1, 1), 'O');
        board.makeMove(makeCoordinate(0, 1), 'O');
        board.makeMove(makeCoordinate(2, 0), 'O');
        assertFalse(board.areAnyMovesLeft(), "No moves left in draw");
        assertEquals(WinType.NONE, board.checkHasPlayerWon(playerX));
        assertEquals(WinType.NONE, board.checkHasPlayerWon(playerO));
    }

    @Test
    void testMovesLeft() throws Exception {
        board.makeMove(makeCoordinate(0, 0), 'X');
        assertTrue(board.areAnyMovesLeft(), "Moves should still be left");
    }
}