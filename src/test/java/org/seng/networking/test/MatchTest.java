package org.seng.networking.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng.networking.Player;
import org.seng.networking.Match;
import org.seng.networking.leaderboard_matchmaking.GameType;

import static org.junit.jupiter.api.Assertions.*;

public class MatchTest {

    private Player player1;
    private Player player2;

    @BeforeEach
    void setup() {
        player1 = new Player("Alice", "1"); // Both arguments must be Strings
        player2 = new Player("Bob", "2");
    }


    @Test
    void testMatchCreation() {
        Match match = new Match(player1, player2, GameType.TICTACTOE);

        assertNotNull(match.getMatchID());
        assertEquals(player1, match.getPlayer1());
        assertEquals(player2, match.getPlayer2());
        assertEquals(GameType.TICTACTOE, match.getGameType());
        assertTrue(match.isReady());
    }

    @Test
    void testMarkAsNotReady() {
        Match match = new Match(player1, player2, GameType.TICTACTOE);
        match.markAsNotReady();
        assertFalse(match.isReady());
    }
}
