package main.java.org.seng.leaderboard_matchmaking;

/**
 * Implementation of game-specific statistics for Checkers.
 * Uses an MMR range of 0 to 200 with 7 rank tiers.
 */
public class checkersStats extends GeneralStats {
    private static final int MAX_MMR = 200;
    private static final int MIN_MMR = 0;
    private static final int TOTAL_RANKS = 7;
    private static final double RANK_STEP = (MAX_MMR - MIN_MMR) / (double) TOTAL_RANKS;

    /**
     * Constructs a new checkersStats object for a player.
     *
     * @param playerID The unique identifier for the player.
     */
    public checkersStats(String playerID) {
        super(playerID);
        this.checkersmmr = 0;
    }

    /**
     * Returns the current Checkers MMR.
     *
     * @return The checkers MMR.
     */
    @Override
    public int getMMR() {
        return checkersmmr;
    }

    /**
     * Updates the Checkers MMR based on the game result.
     * On a win, adds one full rank step; on a loss, subtracts half a rank step.
     *
     * @param win true if the game was won, false if lost.
     */
    @Override
    protected void updateMMR(boolean win) {
        if (win) {
            checkersmmr += (int) Math.round(RANK_STEP / 2.0);
        } else {
            checkersmmr -= (int) Math.round(RANK_STEP / 2.0);
        }
        if (checkersmmr < MIN_MMR) {
            checkersmmr = MIN_MMR;
        } else if (checkersmmr > MAX_MMR) {
            checkersmmr = MAX_MMR;
        }
    }

    /**
     * Updates the Checkers MMR for a tie.
     * For Checkers, a tie does not change the MMR.
     */
    @Override
    protected void updateMMRTies() {
    }

    /**
     * Updates the player's rank based on the current Checkers MMR.
     * Rank is determined by dividing the MMR range into equal steps.
     */
    @Override
    protected void updateRank() {
        double relativeMMR = checkersmmr - MIN_MMR;
        int rankIndex = (int) (relativeMMR / RANK_STEP);
        if (rankIndex < 0) rankIndex = 0;
        if (rankIndex >= TOTAL_RANKS) rankIndex = TOTAL_RANKS - 1;

        switch (rankIndex) {
            case 0 -> rank = Rank.BRONZE;
            case 1 -> rank = Rank.SILVER;
            case 2 -> rank = Rank.GOLD;
            case 3 -> rank = Rank.PLATINUM;
            case 4 -> rank = Rank.DIAMOND;
            case 5 -> rank = Rank.MASTER;
            case 6 -> rank = Rank.GRANDMASTER;
            default -> rank = Rank.BRONZE;
        }
    }

    /**
     * Returns the number of wins in Checkers.
     *
     * @return The win count.
     */
    public int getWins() {
        return wins;
    }

    /**
     * Returns the number of losses in checkers.
     *
     * @return The loss count.
     */
    public int getLosses() {
        return losses;
    }

    @Override
    public void setGamesPlayed(int games_Played) {
        this.gamesPlayed = games_Played;
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
        this.checkersmmr = game_specific_MMR;
    }

    /**
     * Returns a string representation of Checkers statistics.
     *
     * @return String representation of checkersStats.
     */
    @Override
    public String toString() {
        return "CheckersStats [playerID=" + playerID
                + ", gamesPlayed=" + gamesPlayed
                + ", wins=" + wins
                + ", losses=" + losses
                + ", ties=" + ties
                + ", checkersmmr=" + checkersmmr
                + ", rank=" + rank
                + "]";
    }
}