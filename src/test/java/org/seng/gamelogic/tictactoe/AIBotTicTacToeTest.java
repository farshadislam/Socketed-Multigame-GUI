package org.seng.gamelogic.tictactoe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.seng.gamelogic.Player;

public class AIBotTicTacToeTest {

    private TicTacToeBoard board;
    private Player[] players;
    private TicTacToeGame game;

    @BeforeEach
    void setUp() {
        TicTacToeBoard board = new TicTacToeBoard();
        TicTacToePlayer[] players = new TicTacToePlayer[]{new TicTacToePlayer("Player1", 1, 'b', 1), new TicTacToePlayer("Player2", 2, 'y', 1)};
        game = new TicTacToeGame(board, players, 1);
    }


        @Test
        public void testFindNextMoveNotNull () {
            AIBotTicTacToe bot = new AIBotTicTacToe('X', game, board);

            int[] move = bot.findNextMove(board);
            assertNotNull(move);
        }

        @Test
        public void testMakeMoveSuccess () {
            AIBotTicTacToe bot = new AIBotTicTacToe('X', game, board);

            TicTacToeMove move = new TicTacToeMove(1, 1, 'X');
            boolean result = bot.makeMove(board, move);
            assertTrue(result);
        }

        @Test
        public void testNextMoveReturnsMove () {
            AIBotTicTacToe bot = new AIBotTicTacToe('X', game, board);

            bot.setSymbol('X');
            game.makeMove(0, 0); //Makes X's move
            TicTacToeMove move = bot.nextMove(board);
            assertNull(move);
        }

        @Test
        public void testSetAndGetSymbol () {
            AIBotTicTacToe bot = new AIBotTicTacToe('X', game, board);
            assertEquals('X', bot.getSymbol());
            bot.setSymbol('O');
            assertEquals('O', bot.getSymbol());
        }
    }