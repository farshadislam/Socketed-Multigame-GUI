package leaderboard_matchmaking;

public class connect4Stats extends GeneralStats {
    private static final int MAX_MMR = 200;
    private static final int MIN_MMR = 0;
    private static final int TOTAL_RANKS = 7;
    private static final double RANK_STEP = (MAX_MMR - MIN_MMR) / (double) TOTAL_RANKS;

    public connect4Stats(String playerID) {
        super(playerID);
        // We start connect4mmr at the midpoint.
        this.connect4mmr = 0;
    }

    /**
     * Update MMR for Connect 4 when a game ends.
     * @param win true if the player won, false if the player lost
     */
    @Override
    protected void updateMMR(boolean win) {
        // we add one rank step on a win, and subtract half a rank step on a loss.
        if (win) {
            // Gain 1 rank step
            connect4mmr += (int) Math.round(RANK_STEP);
        } else {
            // Lose half a rank step
            connect4mmr -= (int) Math.round(RANK_STEP / 2.0);
        }

        // Enforce MMR boundaries so it doesn't go below MIN or above MAX
        if (connect4mmr < MIN_MMR) {
            connect4mmr = MIN_MMR;
        } else if (connect4mmr > MAX_MMR) {
            connect4mmr = MAX_MMR;
        }
    }

}
