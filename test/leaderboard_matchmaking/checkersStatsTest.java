package leaderboard_matchmaking;

import org.seng.leaderboard_matchmaking.Rank;
import org.seng.leaderboard_matchmaking.checkersStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class checkersStatsTest {
    private checkersStats stats;

    // Test subclass to expose the protected updateRank() method.
    public static class TestableCheckersStats extends checkersStats {
        public TestableCheckersStats(String playerID) {
            super(playerID);
        }
        @Override
        public void updateRank() {
            super.updateRank();
        }
    }

    @BeforeEach
    public void setUp() {
        stats = new checkersStats("player4");
    }

    @Test
    public void testInitialState() {
        // Validate initial state.
        assertEquals(0, stats.getMMR(), "Initial MMR should be 0.");
        assertEquals(Rank.BRONZE, stats.getRank(), "Initial rank should be BRONZE.");
        assertEquals(0, stats.get_wins(), "Initial wins should be 0.");
        assertEquals(0, stats.get_losses(), "Initial losses should be 0.");
        assertEquals(0, stats.get_ties(), "Initial ties should be 0.");
        assertEquals(0, stats.getGamesPlayed(), "Initial games played should be 0.");
    }

    @Test
    public void testWinIncrementsMMR() {
        stats.win();
        assertEquals(14, stats.getMMR(), "After one win, MMR should increase by 14.");
    }

    @Test
    public void testWinIncrementsWinsAndGamesPlayed() {
        stats.win();
        assertEquals(1, stats.get_wins(), "Wins count should be 1 after one win.");
        assertEquals(1, stats.getGamesPlayed(), "Games played should be 1 after one win.");
    }

    @Test
    public void testLossDecrementsMMR() {
        stats.win();  // MMR becomes 14.
        stats.win();  // MMR becomes 28.
        stats.lose(); // Subtracts ~14 to yield 14.
        assertEquals(14, stats.getMMR(), "After two wins and one loss, MMR should be 14.");
    }

    @Test
    public void testLossIncrementsLossesAndGamesPlayed() {
        stats.win();
        stats.win();
        stats.lose();
        assertEquals(1, stats.get_losses(), "Losses count should be 1 after one loss.");
        assertEquals(3, stats.getGamesPlayed(), "Games played should be 3 after two wins and one loss.");
    }

    @Test
    public void testTieDoesNotChangeMMR() {
        stats.tie();
        assertEquals(0, stats.getMMR(), "A tie should not change MMR.");
    }

    @Test
    public void testTieIncrementsTiesAndGamesPlayed() {
        stats.tie();
        assertEquals(1, stats.get_ties(), "Ties count should be 1 after one tie.");
        assertEquals(1, stats.getGamesPlayed(), "Games played should be 1 after one tie.");
    }

    @Test
    public void testMMRClampingAtMaximum() {
        for (int i = 0; i < 20; i++) {
            stats.win();
        }
        assertEquals(200, stats.getMMR(), "MMR should be clamped at 200 after many wins.");
    }

    @Test
    public void testMMRClampingAtMinimum() {
        stats.lose();
        stats.lose();
        stats.lose();
        assertEquals(0, stats.getMMR(), "MMR should never drop below 0.");
    }

    @Test
    public void testRankTransitionAfterWins() {
        stats.win();
        stats.win();
        assertEquals(Rank.BRONZE, stats.getRank(), "After 2 wins, rank should remain BRONZE.");
        stats.win();
        assertEquals(Rank.SILVER, stats.getRank(), "After 3 wins, rank should update to SILVER.");
    }

    @Test
    public void testRecordResultWin() {
        stats.recordResult(true, false);
        assertEquals(14, stats.getMMR(), "recordResult with win should add 14 points.");
        assertEquals(1, stats.get_wins(), "Wins count should be 1.");
        assertEquals(1, stats.getGamesPlayed(), "Games played should be 1.");
    }

    @Test
    public void testRecordResultTie() {
        stats.recordResult(false, true);
        assertEquals(0, stats.getMMR(), "recordResult with tie should not change MMR.");
        assertEquals(1, stats.get_ties(), "Ties count should be 1.");
        assertEquals(1, stats.getGamesPlayed(), "Games played should be 1.");
    }

    @Test
    public void testRecordResultLoss() {
        stats.win(); // Increase MMR to 14.
        stats.recordResult(false, false);
        assertEquals(0, stats.getMMR(), "recordResult with loss should subtract 14 points, resulting in 0 MMR.");
        assertEquals(1, stats.get_losses(), "Losses count should be 1.");
        assertEquals(2, stats.getGamesPlayed(), "Games played should be 2.");
    }

    @Test
    public void testUpdateMMRTiesNoChange() {
        stats.setMMR(50);
        stats.tie();
        assertEquals(50, stats.getMMR(), "MMR should remain unchanged when tied.");
    }

    @Test
    public void testSettersAndGetters() {
        stats.setGamesPlayed(7);
        stats.setWins(3);
        stats.setLosses(2);
        stats.setTies(1);
        stats.setMMR(123);
        stats.setRank(Rank.DIAMOND);

        assertAll("set/get pairs",
                () -> assertEquals(7, stats.getGamesPlayed()),
                () -> assertEquals(3, stats.get_wins()),
                () -> assertEquals(3, stats.getWins()),
                () -> assertEquals(2, stats.get_losses()),
                () -> assertEquals(2, stats.getLosses()),
                () -> assertEquals(1, stats.get_ties()),
                () -> assertEquals(123, stats.getMMR()),
                () -> assertEquals(Rank.DIAMOND, stats.getRank())
        );
    }

    @Test
    public void testUpdateRankBoundary() {
        // Use the Testable subclass to explicitly invoke updateRank.
        TestableCheckersStats testStats = new TestableCheckersStats("testPlayer");
        testStats.setMMR(-5);
        testStats.updateRank();
        assertEquals(Rank.BRONZE, testStats.getRank(), "Negative MMR should result in BRONZE rank.");

        testStats.setMMR(300);
        testStats.updateRank();
        assertEquals(Rank.GRANDMASTER, testStats.getRank(), "Excessive MMR should result in GRANDMASTER rank.");
    }
}
