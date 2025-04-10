package org.seng.gamelogic.connectfour;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AIBotConnectFourTest {

    private ConnectFourBoard board;
    private AIBotConnectFour aiBot;
    private ConnectFourPlayer[] players;
    private ConnectFourGame game;

    @BeforeEach
    public void setUp() {
        board = new ConnectFourBoard();
        players = new ConnectFourPlayer[] {
                new AIBotConnectFour('b', null, board),
                new ConnectFourPlayer("Human", "2", "y")
        };
        game = new ConnectFourGame(board, players, 1);
        aiBot = (AIBotConnectFour) players[0];
        game.currentPlayer = aiBot;
        aiBot = new AIBotConnectFour('b', game, board);  // Rebuild with correct game
    }

    @Test
    public void testAIMakeMoveWorks() {
        boolean success = aiBot.makeMove(board, game);
        assertTrue(success);
    }

    @Test
    public void testFindNextMoveReturnsValidColumn() {
        Integer move = aiBot.findNextMove(board);
        assertNotNull(move);
        assertTrue(move >= 0 && move < ConnectFourBoard.COL_COUNT);
    }

    @Test
    public void testAIBotReturnsNullIfNotCurrentPlayer() {
        game.currentPlayer = new ConnectFourPlayer("NotAI", "3", "z");
        assertNull(aiBot.getMove(board));
    }
}
