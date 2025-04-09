package leaderboard_matchmaking;

import org.seng.leaderboard_matchmaking.GeneralStats;
import org.seng.leaderboard_matchmaking.Rank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GeneralStatsTest {

    /**
     * A concrete subclass of GeneralStats for testing purposes.
     */
    public static class TestGeneralStats extends GeneralStats {
        private int testMMR;  // We'll use this to simulate game-specific MMR

        public TestGeneralStats(String playerID) {
            super(playerID);
            testMMR = 0;
        }

        @Override
        public int getMMR() {
            return testMMR;
        }

        @Override
        protected void updateMMR(boolean win) {
            // For testing: add 10 if win, subtract 10 if loss.
            if (win) {
                testMMR += 10;
            } else {
                testMMR -= 10;
            }
        }

        @Override
        protected void updateMMRTies() {
            // For testing ties, do nothing (MMR remains unchanged).
        }

        // We intentionally call the parent version so that the default (empty) implementation is exercised.
        @Override
        public void updateRank() {
            super.updateRank();
        }

        @Override
        public void setGamesPlayed(int num_of_gamesPlayed) {
            this.gamesPlayed = num_of_gamesPlayed;
        }

        @Override
        public void setWins(int num_of_wins) {
            this.wins = num_of_wins;
        }

        @Override
        public void setLosses(int num_of_losses) {
            this.losses = num_of_losses;
        }

        @Override
        public void setTies(int num_of_ties) {
            this.ties = num_of_ties;
        }

        @Override
        public void setRank(Rank player_rank) {
            this.rank = player_rank;
        }

        @Override
        public void setMMR(int game_specific_MMR) {
            this.testMMR = game_specific_MMR;
        }
    }

    private TestGeneralStats stats;

    @BeforeEach
    public void setUp() {
        stats = new TestGeneralStats("testPlayer");
    }

    @Test
    public void testInitialState() {
        // Validate the constructor and getters.
        assertEquals("testPlayer", stats.getPlayerId());
        assertEquals(0, stats.getGamesPlayed());
        assertEquals(0, stats.get_wins());
        assertEquals(0, stats.get_losses());
        assertEquals(0, stats.get_ties());
        assertEquals(0, stats.getMMR());
        // Default rank should be BRONZE.
        assertEquals(Rank.BRONZE, stats.getRank());
    }

    @Test
    public void testWinMethod() {
        stats.win();
        // After one win: wins and gamesPlayed increment; MMR increases by 10.
        assertEquals(1, stats.get_wins());
        assertEquals(1, stats.getGamesPlayed());
        assertEquals(10, stats.getMMR());
    }

    @Test
    public void testLoseMethod() {
        stats.lose();
        // After one loss: losses and gamesPlayed increment; MMR decreases by 10.
        assertEquals(1, stats.get_losses());
        assertEquals(1, stats.getGamesPlayed());
        assertEquals(-10, stats.getMMR());
    }

    @Test
    public void testTieMethod() {
        stats.tie();
        // After one tie: ties and gamesPlayed increment; MMR remains unchanged.
        assertEquals(1, stats.get_ties());
        assertEquals(1, stats.getGamesPlayed());
        assertEquals(0, stats.getMMR());
    }

    @Test
    public void testRecordResultWin() {
        stats.recordResult(true, false);
        // recordResult(true, false): wins and gamesPlayed increment; MMR increases by 10.
        assertEquals(1, stats.get_wins());
        assertEquals(1, stats.getGamesPlayed());
        assertEquals(10, stats.getMMR());
    }

    @Test
    public void testRecordResultTie() {
        stats.recordResult(false, true);
        // recordResult(false, true): ties and gamesPlayed increment; MMR remains unchanged.
        assertEquals(1, stats.get_ties());
        assertEquals(1, stats.getGamesPlayed());
        assertEquals(0, stats.getMMR());
    }

    @Test
    public void testRecordResultLoss() {
        stats.recordResult(false, false);
        // recordResult(false, false): losses and gamesPlayed increment; MMR decreases by 10.
        assertEquals(1, stats.get_losses());
        assertEquals(1, stats.getGamesPlayed());
        assertEquals(-10, stats.getMMR());
    }

    /**
     * Extra test to ensure that if both win and tie are true,
     * the win branch takes precedence.
     */
    @Test
    public void testRecordResultWinAndTieTrue() {
        stats.recordResult(true, true);
        // win is true; therefore, tie is ignored.
        assertEquals(1, stats.get_wins());
        assertEquals(1, stats.getGamesPlayed());
        assertEquals(10, stats.getMMR());
        assertEquals(0, stats.get_ties());
    }

    /**
     * Directly call updateMMRTies() to ensure its execution is covered.
     */
    @Test
    public void testUpdateMMRTiesDirectCall() {
        stats.updateMMRTies();
        // Since updateMMRTies() does nothing, MMR remains unchanged.
        assertEquals(0, stats.getMMR(), "MMR should remain unchanged after updateMMRTies()");
    }

    @Test
    public void testSettersAndGetters() {
        // Use setters to modify state and then verify via getters.
        stats.setGamesPlayed(10);
        stats.setWins(4);
        stats.setLosses(3);
        stats.setTies(2);
        stats.setMMR(50);
        stats.setRank(Rank.DIAMOND);

        assertEquals(10, stats.getGamesPlayed());
        assertEquals(4, stats.get_wins());
        assertEquals(3, stats.get_losses());
        assertEquals(2, stats.get_ties());
        assertEquals(50, stats.getMMR());
        assertEquals(Rank.DIAMOND, stats.getRank());
    }

    @Test
    public void testUpdateRankDirectCall() {
        // Although updateRank() is empty by default, calling it ensures full coverage.
        stats.setRank(Rank.MASTER);
        stats.updateRank();
        // Since updateRank() contains no logic, rank remains unchanged.
        assertEquals(Rank.MASTER, stats.getRank());
    }

    // --- Additional tests to boost method coverage ---

    @Test
    public void testMultipleWins() {
        stats.win();
        stats.win();
        stats.win();
        // Expect wins = 3, gamesPlayed = 3, and MMR = 30.
        assertEquals(3, stats.get_wins());
        assertEquals(3, stats.getGamesPlayed());
        assertEquals(30, stats.getMMR());
    }

    @Test
    public void testMultipleLosses() {
        stats.lose();
        stats.lose();
        // Expect losses = 2, gamesPlayed = 2, and MMR = -20.
        assertEquals(2, stats.get_losses());
        assertEquals(2, stats.getGamesPlayed());
        assertEquals(-20, stats.getMMR());
    }

    @Test
    public void testMultipleTies() {
        stats.tie();
        stats.tie();
        // Expect ties = 2, gamesPlayed = 2, and MMR remains 0.
        assertEquals(2, stats.get_ties());
        assertEquals(2, stats.getGamesPlayed());
        assertEquals(0, stats.getMMR());
    }

    @Test
    public void testSequentialRecordResults() {
        // Execute a sequence of recordResult calls.
        stats.recordResult(true, false);   // win: +10 MMR, wins = 1
        stats.recordResult(false, true);     // tie: no MMR change, ties = 1
        stats.recordResult(false, false);    // loss: -10 MMR, losses = 1
        // Total: gamesPlayed = 3, MMR = 0, wins = 1, ties = 1, losses = 1.
        assertEquals(3, stats.getGamesPlayed());
        assertEquals(1, stats.get_wins());
        assertEquals(1, stats.get_ties());
        assertEquals(1, stats.get_losses());
        assertEquals(0, stats.getMMR());
    }

    @Test
    public void testIndividualGetters() {
        // Explicitly call each getter.
        String id = stats.getPlayerId();
        int games = stats.getGamesPlayed();
        int wins = stats.get_wins();
        int losses = stats.get_losses();
        int ties = stats.get_ties();
        int mmr = stats.getMMR();
        Rank r = stats.getRank();

        assertNotNull(id);
        assertTrue(games >= 0);
        assertTrue(wins >= 0);
        assertTrue(losses >= 0);
        assertTrue(ties >= 0);
        // Just a basic check for MMR value.
        assertEquals(0, mmr);
        // Default rank is BRONZE.
        assertEquals(Rank.BRONZE, r);
    }
}
