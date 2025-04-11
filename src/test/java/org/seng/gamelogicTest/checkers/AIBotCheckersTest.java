package org.seng.gamelogicTest.checkers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AIBotCheckersTest {

    private CheckersGame game;
    private CheckersBoard board;
    private ExtendedAIBotCheckers aiBot;
    private CheckersPlayer player;

    @BeforeEach
    void setUp() {
        // initialize player 1 with red piece
        player = new CheckersPlayer("player123", 1, 'r', 0);

        // set up checkers game and board
        board = new CheckersBoard();
        game = new CheckersGame(board, new CheckersPlayer[]{player},10);

        // initialize AI bot as player 2 with black piece
        aiBot = new AIBotCheckers('b', game, board);


    }

    // Test valid move
    @Test
    void testValidMove() {
        // set up valid move
        board.setPieceAt(5, 0, CheckersBoard.Piece.BLACK);
        board.setPieceAt(4, 1, CheckersBoard.Piece.EMPTY); // AI moves here next - valid because it is empty

        // AI makes move
        CheckersMove move = new CheckersMove(5, 0, 4, 1, aiBot);
        boolean result = aiBot.makeMove(board, move);
        assertTrue(result, "Move should be successful");
    }

    // Test invalid move
    @Test
    void testInvalidMove() {
        board.setPieceAt(5, 0, CheckersBoard.Piece.BLACK);
        board.setPieceAt(4, 1, CheckersBoard.Piece.RED); // a piece is already here

        CheckersMove move = new CheckersMove(5, 0, 4, 1, aiBot);
        boolean result = aiBot.makeMove(board, move);
        assertFalse(result, "Move should be invalid");
    }

    // Test next move
    @Test
    void testFindNextMove() {
        board.setPieceAt(5, 0, CheckersBoard.Piece.BLACK); // Black piece at (5, 0)
        board.setPieceAt(4, 1, CheckersBoard.Piece.EMPTY); // Empty spot at (4, 1)
        board.setPieceAt(6, 1, CheckersBoard.Piece.EMPTY); // Empty spot at (6, 1)

        // AI makes a move
        int[] nextMove = aiBot.nextMove(board);

        // should choose (4, 1) or (6, 1)
        assertTrue((nextMove[2] == 4 && nextMove[3] == 1) || (nextMove[2] == 6 && nextMove[3] == 1),
                "AI should choose an available column");
    }

    @Test
    void testNoAvailableMoves() {
        // set all pieces as blocked (no available moves for AI)
        // check over logic of this
        for (int row = 0; row < CheckersBoard.BOARD_SIZE; row++) {
            for (int col = 0; col < CheckersBoard.BOARD_SIZE; col++) {
                if ((row + col) % 2 == 1) { // Only dark squares are used
                    board.setPieceAt(row, col, CheckersBoard.Piece.RED); // Assume Red pieces fill the board
                }
            }
        }

        // no valid moves free on board
        int[] nextMove = aiBot.nextMove(board);
        assertNull(nextMove, "No valid moves are available");
    }

}
