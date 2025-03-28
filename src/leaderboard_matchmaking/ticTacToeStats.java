package leaderboard_matchmaking;

public class ticTacToeStats extends GeneralStats{
    private static final int MAX_MMR = 200;
    private static final int MIN_MMR = 0;
    private static final int TOTAL_RANKS = 7;
    private static final double RANK_STEP = (MAX_MMR - MIN_MMR) / (double) TOTAL_RANKS;

    public ticTacToeStats(String playerID) {
        super(playerID);
        // Initialize Tic Tac Toe–specific MMR.
        this.tictactoemmr = 0;
    }

    @Override
    protected void updateMMR(boolean win) {
        if (win) {
            // Gain one rank step on a win.
            tictactoemmr += (int) Math.round(RANK_STEP);
        } else {
            // Lose half a rank step on a loss.
            tictactoemmr -= (int) Math.round(RANK_STEP / 2.0);
        }
        // Clamp tictactoemmr within the allowed range.
        if (tictactoemmr < MIN_MMR) {
            tictactoemmr = MIN_MMR;
        } else if (tictactoemmr > MAX_MMR) {
            tictactoemmr = MAX_MMR;
        }
    }
}
