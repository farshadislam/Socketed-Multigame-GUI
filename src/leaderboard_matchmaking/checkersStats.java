package leaderboard_matchmaking;

public class checkersStats extends GeneralStats {
    private static final int MAX_MMR = 200;
    private static final int MIN_MMR = 0;
    private static final int TOTAL_RANKS = 7;
    private static final double RANK_STEP = (MAX_MMR - MIN_MMR) / (double) TOTAL_RANKS;

    public checkersStats(String playerID) {
        super(playerID);
        this.checkersmmr = 0;
    }

    @Override
    public int getMMR() {
        return checkersmmr;
    }

    @Override
    protected void updateMMR(boolean win){
        if (win){
            checkersmmr += (int) Math.round(RANK_STEP);
        }
        else {
            checkersmmr -= (int) Math.round(RANK_STEP);
        }
        if (checkersmmr < MIN_MMR){
            checkersmmr = MIN_MMR;
        }
        else if (checkersmmr > MAX_MMR){
            checkersmmr = MAX_MMR;
        }
    }

    @Override
    protected void updateMMRTies() {
    }

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

