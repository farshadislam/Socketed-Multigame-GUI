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

    @Override
    protected void updateMMR(boolean win){
        if (win){
            // Gain one rank step on win
            checkersmmr += (int) Math.round(RANK_STEP);
        }
        else {
            // Lose half a rank step on loss
            checkersmmr -= (int) Math.round(RANK_STEP);
        }
        // Enforcing mmr to stay within boundaries of min and max mmr
        if (checkersmmr < MIN_MMR){
            checkersmmr = MIN_MMR;
        }
        else if (checkersmmr > MAX_MMR){
            checkersmmr = MAX_MMR;
        }
    }



}

