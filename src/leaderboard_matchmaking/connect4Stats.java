package leaderboard_matchmaking;

public class connect4Stats extends GeneralStats {
    private static final int MAX_MMR = 200;
    private static final int MIN_MMR = 0;
    private static final int TOTAL_RANKS = 7;
    private static final double RANK_STEP = (MAX_MMR - MIN_MMR) / (double) TOTAL_RANKS;

    public connect4Stats(String playerID) {
        super(playerID);
        this.connect4mmr = 0;
    }

    @Override
    protected void updateMMR(boolean win) {
        if (win) {
            connect4mmr += (int) Math.round(RANK_STEP);
        } else {
            connect4mmr -= (int) Math.round(RANK_STEP / 2.0);
        }
        if (connect4mmr < MIN_MMR) {
            connect4mmr = MIN_MMR;
        } else if (connect4mmr > MAX_MMR) {
            connect4mmr = MAX_MMR;
        }
    }

    @Override
    protected void updateMMRTies() {
    }

    @Override
    protected void updateRank() {
        double relativeMMR = connect4mmr - MIN_MMR;
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
        return "Connect4Stats [playerID=" + playerID
                + ", gamesPlayed=" + gamesPlayed
                + ", wins=" + wins
                + ", losses=" + losses
                + ", ties=" + ties
                + ", connect4mmr=" + connect4mmr
                + ", rank=" + rank
                + "]";
    }
}
