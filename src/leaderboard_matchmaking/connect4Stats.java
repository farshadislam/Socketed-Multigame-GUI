package leaderboard_matchmaking;

public class connect4Stats extends GeneralStats {
    private static final int MAX_MMR = 200;
    private static final int MIN_MMR = 0;
    private static final int TOTAL_RANKS = 7;
    private static final double RANK_STEP = (MAX_MMR - MIN_MMR) / (double) TOTAL_RANKS;

    public connect4Stats(String playerID) {
        super(playerID);
        // We start connect4mmr at the midpoint, or wherever you prefer.
        this.connect4mmr = 0;  // or 100, etc.
    }
}
