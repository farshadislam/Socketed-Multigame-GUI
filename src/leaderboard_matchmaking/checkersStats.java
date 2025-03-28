package leaderboard_matchmaking;

public class checkersStats extends GeneralStats {
    private static final int MAX_MMR = 200;
    private static final int MIN_MMR = 0;
    private static final int TOTAL_RANKS = 7;
    private static final double RANK_STEP = (MAX_MMR - MIN_MMR) / (double) TOTAL_RANKS;

    public checkersStats(String playerID) {
        super(playerID);
        // Initialize checkers-specific MMR.
        this.checkersmmr = 0;
    }

}

