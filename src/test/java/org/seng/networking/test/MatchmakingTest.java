package org.seng.networking.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.networking.Match;
import org.seng.networking.Player;
import org.seng.networking.leaderboard_matchmaking.GameType;
import org.seng.networking.Matchmaking;

import static org.junit.jupiter.api.Assertions.*;

class MatchmakingTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        // Reset the singleton instance via reflection for clean state between tests
        var field = Matchmaking.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    void testSingletonInstance() {
        Matchmaking m1 = Matchmaking.getInstance();
        Matchmaking m2 = Matchmaking.getInstance();
        assertSame(m1, m2, "Should return the same singleton instance");
    }

    @Test
    void testJoinQueue_NoOpponent() {
        Matchmaking matchmaking = Matchmaking.getInstance();
        Player player1 = new Player("Alice", "3456789");

        Match match = matchmaking.joinQueue(player1, GameType.TICTACTOE);
        assertNull(match, "No match should be made when only one player joins the queue");
        assertNull(player1.getCurrentMatch(), "Player should not be in a match yet");
    }

    @Test
    void testJoinQueue_WithOpponent() {
        Matchmaking matchmaking = Matchmaking.getInstance();
        Player player1 = new Player("Alice", "8r237bd");
        Player player2 = new Player("Bob", "sv9n4734");

        matchmaking.joinQueue(player1, GameType.TICTACTOE); // joins first, no match
        Match match = matchmaking.joinQueue(player2, GameType.TICTACTOE); // triggers match

        assertNotNull(match, "Match should be created when two players join");
        assertEquals(GameType.TICTACTOE, match.getGameType(), "Match game type should match");

        assertEquals(player1, match.getPlayer1());
        assertEquals(player2, match.getPlayer2());

        assertSame(match, player1.getCurrentMatch());
        assertSame(match, player2.getCurrentMatch());
    }

    @Test
    void testJoinQueue_DifferentGameTypes() {
        Matchmaking matchmaking = Matchmaking.getInstance();
        Player player1 = new Player("Alice", "afnu3923q");
        Player player2 = new Player("Bob", "sv9n4734");

        matchmaking.joinQueue(player1, GameType.TICTACTOE); // queued for Tic Tac Toe
        Match match = matchmaking.joinQueue(player2, GameType.CHECKERS); // queued for Chess

        assertNull(match, "Players in different queues should not match");
        assertNull(player1.getCurrentMatch());
        assertNull(player2.getCurrentMatch());
    }

    @Test
    void testJoinQueue_NullGameType() {
        Matchmaking matchmaking = Matchmaking.getInstance();
        Player player = new Player("Eve", "efaoimlwo38");

        Match match = matchmaking.joinQueue(player, null);
        assertNull(match, "Joining with null GameType should return null");
    }
}
