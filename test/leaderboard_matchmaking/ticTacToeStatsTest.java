package leaderboard_matchmaking;

import org.seng.leaderboard_matchmaking.Rank;
import org.seng.leaderboard_matchmaking.ticTacToeStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ticTacToeStatsTest {
    private ticTacToeStats stats;

    @BeforeEach
    public void setUp() {
        stats = new ticTacToeStats("player3");
    }

    @Test
    public void testInitialState() {
        // Validate initial state.
        assertEquals(0, stats.getMMR(), "Initial MMR should be 0.");
        assertEquals(Rank.BRONZE, stats.getRank(), "Initial rank should be BRONZE.");
        assertEquals(0, stats.getWins(), "Initial wins should be 0.");
        assertEquals(0, stats.getLosses(), "Initial losses should be 0.");
        assertEquals(0, stats.get_ties(), "Initial ties should be 0.");
        assertEquals(0, stats.getGamesPlayed(), "Initial games played should be 0.");
    }

    @Test
    public void testWinIncrementsMMR() {
        stats.win();
        // A win should add approximately 14 points.
        assertEquals(14, stats.getMMR(), "After one win, MMR should increase by 14.");
    }

    @Test
    public void testWinIncrementsWinsAndGamesPlayed() {
        stats.win();
        assertEquals(1, stats.getWins(), "Wins count should be 1 after one win.");
        assertEquals(1, stats.getGamesPlayed(), "Games played should be 1 after one win.");
    }

    @Test
    public void testLossDecrementsMMR() {
        // Increase MMR first.
        stats.win();  // MMR becomes 14.
        stats.win();  // MMR becomes 28.
        stats.lose(); // Loss subtracts ~14, resulting in 14.
        assertEquals(14, stats.getMMR(), "After two wins and one loss, MMR should be 14.");
    }

    @Test
    public void testLossIncrementsLossesAndGamesPlayed() {
        stats.win();
        stats.win();
        stats.lose();
        assertEquals(1, stats.getLosses(), "Losses count should be 1 after one loss.");
        assertEquals(3, stats.getGamesPlayed(), "Games played should be 3 after two wins and one loss.");
    }

    @Test
    public void testTieDoesNotChangeMMR() {
        stats.tie();
        // Tie does not alter MMR.
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
        // Many wins should cap the MMR at 200.
        for (int i = 0; i < 20; i++) {
            stats.win();
        }
        assertEquals(200, stats.getMMR(), "MMR should be clamped at 200 after many wins.");
    }

    @Test
    public void testMMRClampingAtMinimum() {
        // Repeated losses should not reduce MMR below 0.
        stats.lose();
        stats.lose();
        stats.lose();
        assertEquals(0, stats.getMMR(), "MMR should never drop below 0.");
    }

    @Test
    public void testRankTransitionAfterWins() {
        stats.win(); // MMR becomes 14, rank should be BRONZE.
        stats.win(); // MMR becomes 28, still BRONZE.
        assertEquals(Rank.BRONZE, stats.getRank(), "After 2 wins, rank should remain BRONZE.");
        stats.win(); // MMR becomes 42, which should transition rank.
        assertEquals(Rank.SILVER, stats.getRank(), "After 3 wins, rank should update to SILVER.");
    }

    @Test
    public void testRecordResultWin() {
        stats.recordResult(true, false);
        assertEquals(14, stats.getMMR(), "recordResult with win should add 14 points.");
        assertEquals(1, stats.getWins(), "Wins count should be 1.");
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
        assertEquals(1, stats.getLosses(), "Losses count should be 1.");
        assertEquals(2, stats.getGamesPlayed(), "Games played should be 2.");
    }

    @Test
    public void testUpdateMMRTiesNoChange() {
        stats.setMMR(50);
        stats.tie();   // updateMMRTies is called internally.
        assertEquals(50, stats.getMMR(), "MMR should remain unchanged on tie.");
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
                () -> assertEquals(3, stats.getWins()),
                () -> assertEquals(2, stats.getLosses()),
                () -> assertEquals(1, stats.get_ties()),
                () -> assertEquals(123, stats.getMMR()),
                () -> assertEquals(Rank.DIAMOND, stats.getRank())
        );
    }

    @Test
    public void testUpdateRankBoundary() {
        // Test lower boundary: negative MMR should clamp to BRONZE.
        stats.setMMR(-10);
        stats.updateRank();
        assertEquals(Rank.BRONZE, stats.getRank(), "Negative MMR should result in BRONZE rank.");

        // Test upper boundary: excessive MMR should produce GRANDMASTER.
        stats.setMMR(300);
        stats.updateRank();
        assertEquals(Rank.GRANDMASTER, stats.getRank(), "MMR greater than max should result in GRANDMASTER rank.");
    }
}
