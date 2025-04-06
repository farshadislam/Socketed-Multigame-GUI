package org.seng.gamelogic.tictactoe;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.seng.gamelogic.tictactoe.*;

public class ExtendedAIBotTicTacToeTest {

    @Test
    public void testFindNextMoveNotNull() {
        TicTacToeGame game = new TicTacToeGame();
        TicTacToeBoard board = new TicTacToeBoard();
        ExtendedAIBotTicTacToe bot = new ExtendedAIBotTicTacToe('X', game, board);

        int[] move = bot.findNextMove(board);
        assertNotNull(move);
    }

    @Test
    public void testMakeMoveSuccess() {
        TicTacToeGame game = new TicTacToeGame();
        TicTacToeBoard board = new TicTacToeBoard();
        ExtendedAIBotTicTacToe bot = new ExtendedAIBotTicTacToe('X', game, board);

        TicTacToeMove move = new TicTacToeMove(1, 1, 'X');
        boolean result = bot.makeMove(board, move);
        assertTrue(result);
    }

    @Test
    public void testNextMoveReturnsMove() {
        TicTacToeGame game = new TicTacToeGame();
        TicTacToeBoard board = new TicTacToeBoard();
        ExtendedAIBotTicTacToe bot = new ExtendedAIBotTicTacToe('X', game, board);

        bot.setSymbol('X');
        game.makeMove(0, 0); //Makes X's move
        TicTacToeMove move = bot.nextMove(board);
        assertNull(move);
    }

    @Test
    public void testSetAndGetSymbol() {
        ExtendedAIBotTicTacToe bot = new ExtendedAIBotTicTacToe('X', new TicTacToeGame(), new TicTacToeBoard());
        assertEquals('X', bot.getSymbol());
        bot.setSymbol('O');
        assertEquals('O', bot.getSymbol());
    }
}