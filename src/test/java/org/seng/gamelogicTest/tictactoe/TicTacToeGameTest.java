package org.seng.gamelogicTest.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TicTacToeGameTest {

    private TicTacToeGame game;

    //BeforeEnch just runs before every test
    @BeforeEach
    public void setup() {
    TicTacToeBoard board = new TicTacToeBoard();
    TicTacToePlayer[] players = new TicTacToePlayer[]{new TicTacToePlayer("Player1", "1", "b"), new TicTacToePlayer("Player2", "2", "y")};
    game = new TicTacToeGame(board, players, 2);
    }

    @Test
    public void testInitialState() {
        assertEquals("In Progress", game.getStatus());
        assertEquals(TicTacToeBoard.Mark.X, game.getCurrentMark());
    }

    @Test
    public void testMakeMoveAndSwitchTurn() {
        assertTrue(game.makeMove(0, 0));
        assertEquals(TicTacToeBoard.Mark.O, game.getCurrentMark());
    }

    @Test
    public void testWinCondition() {
        game.makeMove(0, 0); // X
        game.makeMove(1, 0); // O
        game.makeMove(0, 1); // X
        game.makeMove(1, 1); // O
        game.makeMove(0, 2); // X wins

        assertEquals("X Wins", game.getStatus());
    }

    @Test
    public void testDrawCondition() {
        game.makeMove(0, 0); // X
        game.makeMove(0, 1); // O
        game.makeMove(0, 2); // X
        game.makeMove(1, 1); // O
        game.makeMove(1, 0); // X
        game.makeMove(1, 2); // O
        game.makeMove(2, 1); // X
        game.makeMove(2, 0); // O
        game.makeMove(2, 2); // X

        assertEquals("Draw", game.getStatus());
    }

    @Test
    public void testSendMessage() {
        game.sendMessage("Hello!");
        assertEquals(1, game.getChatLog().size());
    }
}